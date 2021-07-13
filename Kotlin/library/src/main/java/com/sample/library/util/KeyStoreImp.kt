package com.sample.library.util

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.sample.library.app
import com.sample.library.extension.androidId
import com.sample.library.extension.packageName
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.spec.AlgorithmParameterSpec
import java.util.*
import javax.crypto.Cipher
import javax.security.auth.x500.X500Principal

object KeyStoreImp {

    private val keyProperties get() = KeyProperties.KEY_ALGORITHM_RSA

    private val aliasKeyStore get() = app.packageName

    private val transformation get() = "RSA/ECB/PKCS1Padding"

    private val provider get() = "AndroidKeyStore"

    private val keyStore: KeyStore get() = KeyStore.getInstance(provider)

    private val keyPairGenerator: KeyPairGenerator
        get() = KeyPairGenerator.getInstance(
            keyProperties,
            provider
        )

    private val algorithmParameterSpec: AlgorithmParameterSpec
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                KeyGenParameterSpec.Builder(
                    aliasKeyStore,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()
            } else {
                val startTime = Calendar.getInstance()
                val endTime = Calendar.getInstance()
                endTime.add(Calendar.YEAR, 5)
                @Suppress("DEPRECATION")
                KeyPairGeneratorSpec.Builder(app)
                    .setAlias(aliasKeyStore)
                    .setSubject(X500Principal("CN=${app.packageName}, O=Android Authority"))
                    .setSerialNumber(BigInteger.ONE)
                    .setStartDate(startTime.time)
                    .setEndDate(endTime.time)
                    .setKeySize(2048)
                    .build()
            }
        }

    val keyAliases: Enumeration<String> get() = keyStore.aliases()

    val publicKey: String
        get() {
            val cert = keyStore.getCertificate(aliasKeyStore)
            val publicKeyBytes: ByteArray = Base64.encode(cert.publicKey.encoded, Base64.NO_WRAP)
            return String(publicKeyBytes)
        }

    init {
        keyStore.load(null)
        initKeyStore()
    }

    fun initKeyStore() {

        if (keyStore.containsAlias(aliasKeyStore)) return

        keyPairGenerator.initialize(algorithmParameterSpec)

        val keyPair: KeyPair = keyPairGenerator.generateKeyPair()
    }

    fun removeKeyStore() {

        if (!keyStore.containsAlias(aliasKeyStore)) return

        keyStore.deleteEntry(aliasKeyStore)
    }

    fun encrypt(clearText: String): String {
        initKeyStore()
        val publicKey = keyStore.getCertificate(aliasKeyStore).publicKey
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val cipherText = cipher.doFinal(clearText.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(cipherText, Base64.DEFAULT)
    }

    fun decrypt(cipherText: String): String {
        initKeyStore()
        val privateKeyEntry = keyStore.getEntry(aliasKeyStore, null) as KeyStore.PrivateKeyEntry
        val privateKey = privateKeyEntry.privateKey
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val cipherTextBytes: ByteArray = Base64.decode(cipherText, Base64.DEFAULT)
        val decryptText: ByteArray = cipher.doFinal(cipherTextBytes)
        return String(decryptText)
    }

    /**
     * [SharedPreferences]
     */
    val basekey: String get() = "$packageName.$androidId.ks"

    private val shared = app.getSharedPreferences(basekey, Context.MODE_PRIVATE)

    fun save(key: String, clearText: String) {
        shared.edit().also {
            it.putString(key, encrypt(clearText))
            it.commit()
            it.apply()
        }
    }

    fun get(key: String): String? {
        return shared.getString(key, null)
    }

}