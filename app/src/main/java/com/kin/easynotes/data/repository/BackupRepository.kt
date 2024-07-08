package com.kin.easynotes.data.repository

import android.content.Context
import android.net.Uri
import com.kin.easynotes.core.constant.DatabaseConst
import com.kin.easynotes.data.local.database.NoteDatabaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class BackupRepository(
    private val provider: NoteDatabaseProvider,
    private val context: Context,
    private val mutex: Mutex,
    private val scope: CoroutineScope,
    private val dispatcher: ExecutorCoroutineDispatcher,
) {

    suspend fun export(uri: Uri) {
        withContext(dispatcher + scope.coroutineContext) {
            mutex.withLock {
                provider.close()

                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    ZipOutputStream(outputStream).use { zipOutputStream ->
                        val databaseFile = context.getDatabasePath(DatabaseConst.NOTES_DATABASE_FILE_NAME)
                        FileInputStream(databaseFile).use { inputStream ->
                            val zipEntry = ZipEntry(databaseFile.name)
                            zipOutputStream.putNextEntry(zipEntry)
                            inputStream.copyTo(zipOutputStream)
                            zipOutputStream.closeEntry()
                        }
                    }
                }
            }
        }
    }

    suspend fun import(uri: Uri) {
        withContext(dispatcher +  scope.coroutineContext) {
            mutex.withLock {
                provider.close()

                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    ZipInputStream(inputStream).use { zipInputStream ->
                        var entry: ZipEntry?
                        while (zipInputStream.nextEntry.also { entry = it } != null) {
                            val dbFile = context.getDatabasePath(DatabaseConst.NOTES_DATABASE_FILE_NAME)
                            if (dbFile != null) {
                                dbFile.delete()

                                dbFile.outputStream().use { outputStream ->
                                    zipInputStream.copyTo(outputStream)
                                }
                            } else {
                                throw IllegalStateException("Database file path is null or invalid.")
                            }
                            zipInputStream.closeEntry()
                            println("Writing Backup Finished")
                        }
                    }
                }
            }
        }
    }

}