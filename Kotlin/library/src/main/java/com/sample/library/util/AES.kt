package com.sample.library.util

import android.security.keystore.KeyProperties
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AES {

    private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES

    private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"

    fun generateKey(): ByteArray {
        val keyGen = KeyGenerator.getInstance(ALGORITHM)
        keyGen.init(256)
        val secretKey = keyGen.generateKey()
        return secretKey.encoded
    }

    fun encrypt(plainText: ByteArray, key: ByteArray): ByteArray {

        // Generating IV.
        val ivSize = 16
        val iv = ByteArray(ivSize)
        val random = SecureRandom()
        random.nextBytes(iv)
        val ivParameterSpec = IvParameterSpec(iv)
        val secretKeySpec = SecretKeySpec(key, ALGORITHM)

        // Encrypt.
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
        val encrypted = cipher.doFinal(plainText)

        // Combine IV and encrypted part.
        val encryptedIVAndText = ByteArray(ivSize + encrypted.size)
        System.arraycopy(iv, 0, encryptedIVAndText, 0, ivSize)
        System.arraycopy(encrypted, 0, encryptedIVAndText, ivSize, encrypted.size)

        return encryptedIVAndText
    }

    fun decrypt(encryptedIvTextBytes: ByteArray, key: ByteArray): ByteArray {
        val ivSize = 16

        // Extract IV.
        val iv = ByteArray(ivSize)
        System.arraycopy(encryptedIvTextBytes, 0, iv, 0, iv.size)
        val ivParameterSpec = IvParameterSpec(iv)

        // Extract encrypted part.
        val encryptedSize = encryptedIvTextBytes.size - ivSize
        val encryptedBytes = ByteArray(encryptedSize)
        System.arraycopy(encryptedIvTextBytes, ivSize, encryptedBytes, 0, encryptedSize)
        val secretKeySpec = SecretKeySpec(key, ALGORITHM)

        // Decrypt.
        val cipherDecrypt = Cipher.getInstance(TRANSFORMATION)
        cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)

        return cipherDecrypt.doFinal(encryptedBytes)
    }
}