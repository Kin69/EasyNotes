package com.kin.easynotes.presentation.components

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionHelper(private val mutableVaultPassword: StringBuilder) {

    fun isPasswordEmpty(): Boolean {
        return mutableVaultPassword.isEmpty()
    }

    fun removePassword() {
        mutableVaultPassword.setLength(0)
    }

    fun setPassword(newPassword: String) {
        mutableVaultPassword.setLength(0)
        mutableVaultPassword.append(newPassword)
    }

    fun encrypt(data: String): String {
        val secretKey = generateSecretKey(mutableVaultPassword.toString())
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val ivBytes = cipher.iv
        val encryptedBytes = cipher.doFinal(data.toByteArray(StandardCharsets.UTF_8))
        return "${Base64.encodeToString(ivBytes, Base64.DEFAULT)}:${Base64.encodeToString(encryptedBytes, Base64.DEFAULT)}"
    }

    fun decrypt(data: String): Pair<String?, DecryptionResult> {
        if (mutableVaultPassword.isEmpty()) return Pair(null, DecryptionResult.LOADING)
        if (data.isBlank()) return Pair(null, DecryptionResult.BLANK_DATA)

        return try {
            val split = data.split(":")
            if (split.size != 2) return Pair(null, DecryptionResult.INVALID_DATA)
            val ivBytes = Base64.decode(split[0], Base64.DEFAULT)
            val encryptedBytes = Base64.decode(split[1], Base64.DEFAULT)
            val secretKey = generateSecretKey(mutableVaultPassword.toString())
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val spec = GCMParameterSpec(128, ivBytes)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            val decryptedData = String(decryptedBytes, StandardCharsets.UTF_8)
            Pair(decryptedData, DecryptionResult.SUCCESS)
        } catch (e: Exception) {
            Pair(null, DecryptionResult.BAD_PASSWORD)
        }
    }

    private fun generateSecretKey(password: String): SecretKey {
        val keySpec = SecretKeySpec(password.toByteArray(StandardCharsets.UTF_8), "AES")
        val sha256 = MessageDigest.getInstance("SHA-256")
        val keyBytes = sha256.digest(keySpec.encoded)
        return SecretKeySpec(keyBytes, "AES")
    }
}

enum class DecryptionResult {
    EMPTY,
    SUCCESS,
    INVALID_DATA,
    BLANK_DATA,
    BAD_PASSWORD,
    LOADING
}
