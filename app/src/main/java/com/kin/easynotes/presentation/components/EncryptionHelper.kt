package com.kin.easynotes.presentation.components

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionHelper(context: Context, key: String? = null) {

    private val sharedPreferences = getEncryptedSharedPreferences(context)
    private var password: String? = null

    init {
        password = getPasswordFromSharedPreferences()
        if (password == null) {
            savePassword(key ?: "password")
        }
    }

    fun checkPassword(inputPassword: String): Boolean {
        return inputPassword == password
    }

    fun encrypt(data: String): String {
        val secretKey = generateSecretKey()
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val ivBytes = cipher.iv
        val encryptedBytes = cipher.doFinal(data.toByteArray(StandardCharsets.UTF_8))
        val encryptedData = android.util.Base64.encodeToString(encryptedBytes, android.util.Base64.DEFAULT)
        return "${android.util.Base64.encodeToString(ivBytes, android.util.Base64.DEFAULT)}:$encryptedData"
    }

    fun decrypt(data: String): String {
        if (data.isBlank()) return "Encrypted data cannot be blank or null."
        val split = data.split(":")
        if (split.size != 2) return "Invalid encrypted data format."
        val ivBytes = android.util.Base64.decode(split[0], android.util.Base64.DEFAULT)
        val encryptedBytes = android.util.Base64.decode(split[1], android.util.Base64.DEFAULT)
        val secretKey = generateSecretKey()
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(128, ivBytes)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, StandardCharsets.UTF_8)
    }

    private fun savePassword(password: String) {
        this.password = password
        sharedPreferences.edit().putString(KEY_PASSWORD, password).apply()
    }

    private fun getPasswordFromSharedPreferences(): String? {
        return sharedPreferences.getString(KEY_PASSWORD, null)
    }

    private fun generateSecretKey(): SecretKey {
        val keySpec = SecretKeySpec(password!!.toByteArray(StandardCharsets.UTF_8), "AES")
        val sha256 = MessageDigest.getInstance("SHA-256")
        val keyBytes = sha256.digest(keySpec.encoded)
        return SecretKeySpec(keyBytes, "AES")
    }

    private fun getEncryptedSharedPreferences(context: Context): SharedPreferences {
        val masterKeyAlias = MasterKey.Builder(context, MASTER_KEY_ALIAS)
            .setKeyGenParameterSpec(keyGenParameterSpec)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            SHARED_PREF_FILE_NAME,
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    companion object {
        const val MASTER_KEY_ALIAS = "yourKeyAlias"
        const val KEY_SIZE = 256

        private val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(KEY_SIZE)
            .build()

        private const val SHARED_PREF_FILE_NAME = "encryption_preferences"
        private const val KEY_PASSWORD = "password"
    }
}
