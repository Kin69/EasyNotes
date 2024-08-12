package com.kin.easynotes.data.repository

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.core.database.getStringOrNull
import com.kin.easynotes.core.constant.DatabaseConst
import com.kin.easynotes.data.local.database.NoteDatabaseProvider
import com.kin.easynotes.domain.model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.security.InvalidKeyException
import java.security.SecureRandom
import java.security.spec.KeySpec
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

sealed class BackupResult {
    data class Success(val message: String) : BackupResult()
    data class Error(val message: String) : BackupResult()
    object BadPassword : BackupResult()
}

class ImportExportRepository(
    private val provider: NoteDatabaseProvider,
    private val context: Context,
    private val mutex: Mutex,
    private val scope: CoroutineScope,
    private val dispatcher: ExecutorCoroutineDispatcher,
) {
    private val salt = ByteArray(16).apply { SecureRandom().nextBytes(this) }

    private fun generateSecretKey(password: String, salt: ByteArray): SecretKey {
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
        return SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
    }

    private fun encrypt(data: ByteArray, secretKey: SecretKey): ByteArray {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val iv = ByteArray(16).apply { SecureRandom().nextBytes(this) }
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))
        val encryptedData = cipher.doFinal(data)
        return iv + encryptedData
    }

    private fun decrypt(data: ByteArray, secretKey: SecretKey): ByteArray {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val iv = data.copyOfRange(0, 16)
        val encryptedData = data.copyOfRange(16, data.size)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        return cipher.doFinal(encryptedData)
    }
    suspend fun exportBackup(uri: Uri, password: String?): BackupResult {
        return try {
            withContext(dispatcher + scope.coroutineContext) {
                mutex.withLock {
                    provider.close()

                    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                        val databaseFile = context.getDatabasePath(DatabaseConst.NOTES_DATABASE_FILE_NAME)
                        val tempZipFile = File.createTempFile("backup", ".zip", context.cacheDir)

                        ZipOutputStream(FileOutputStream(tempZipFile)).use { zipOutputStream ->
                            FileInputStream(databaseFile).use { inputStream ->
                                val zipEntry = ZipEntry(databaseFile.name)
                                zipOutputStream.putNextEntry(zipEntry)
                                inputStream.copyTo(zipOutputStream)
                                zipOutputStream.closeEntry()
                            }
                        }

                        val zipData = tempZipFile.readBytes()
                        if (password != null) {
                            val secretKey = generateSecretKey(password, salt)
                            val encryptedData = encrypt(zipData, secretKey)
                            outputStream.write(salt + encryptedData)
                        } else {
                            outputStream.write(zipData)
                        }
                        tempZipFile.delete()
                    }
                }
            }
            BackupResult.Success("Export successful")
        } catch (e: Exception) {
            BackupResult.Error("Export failed: ${e.message}")
        }
    }

    suspend fun importBackup(uri: Uri, password: String?): BackupResult {
        return try {
            withContext(dispatcher + scope.coroutineContext) {
                mutex.withLock {
                    provider.close()

                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                        val inputData = inputStream.readBytes()
                        val data: ByteArray

                        if (password != null) {
                            val salt = inputData.copyOfRange(0, 16)
                            val encryptedData = inputData.copyOfRange(16, inputData.size)
                            val secretKey = generateSecretKey(password, salt)
                            data = decrypt(encryptedData, secretKey)
                        } else {
                            data = inputData
                        }

                        val tempZipFile = File.createTempFile("backup", ".zip", context.cacheDir)
                        tempZipFile.writeBytes(data)

                        ZipInputStream(FileInputStream(tempZipFile)).use { zipInputStream ->
                            var entry: ZipEntry?
                            while (zipInputStream.nextEntry.also { entry = it } != null) {
                                val dbFile = context.getDatabasePath(DatabaseConst.NOTES_DATABASE_FILE_NAME)
                                if (dbFile != null) {
                                    dbFile.delete()

                                    dbFile.outputStream().use { outputStream ->
                                        zipInputStream.copyTo(outputStream)
                                    }
                                } else {
                                    println("Database is not in backup")
                                }
                                zipInputStream.closeEntry()
                                println("Writing Backup Finished")
                            }
                        }
                        tempZipFile.delete()
                    }
                }
            }
            BackupResult.Success("Import successful")
        } catch (e: Exception) {
            when (e) {
                is BadPaddingException,
                is IllegalBlockSizeException,
                is InvalidKeyException -> BackupResult.BadPassword
                else -> BackupResult.Error("Import failed: ${e.message}")
            }
        }
    }

    fun importFile(uri: Uri): Note {
        val content = context.contentResolver.openInputStream(uri)?.use { fileInputStream ->
            fileInputStream.reader().buffered().readText()
        } ?: throw IOException("ContentResolver couldn't open InputStream to import file.")

        val name = context.contentResolver.query(uri, arrayOf(MediaStore.Files.FileColumns.DISPLAY_NAME), null, null)?.use { cursor ->
            if (cursor.moveToFirst()) cursor.getStringOrNull(0) else ""
        } ?: ""

        return Note(
            name = name,
            description = content,
        )
    }
}
