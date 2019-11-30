package com.huy.library.util

/**
 * -------------------------------------------------------------------------------------------------
 *
 * @Project: Kotlin
 * @Created: Huy QV 2019/03/05
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */

import android.os.Build
import android.util.Base64
import java.math.BigInteger
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPublicKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

object RSA {

    private const val CHARSET = "UTF-8"

    private const val CRYPTO_METHOD = "RSA"

    private const val CYPHER = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING"

    // replace this variant with value 4096, this will encrypt in 4096bits, note however that is slower.*/
    private const val CRYPTO_BITS = 2048

    var publicKey = "PublicKey"

    var privateKey = "PrivateKey"

    val keyPair: KeyPair?
        get() {
            return try {
                val kpg = KeyPairGenerator.getInstance(CRYPTO_METHOD)
                kpg.initialize(CRYPTO_BITS)
                kpg.generateKeyPair()
            } catch (ignore: Exception) {
                null
            }
        }

    init {
        val kp = keyPair
        val publicKey = kp!!.public
        val publicKeyBytes = publicKey.encoded
        this.publicKey = String(Base64.encode(publicKeyBytes, Base64.DEFAULT))

        // Save the public key so it is not generated each and every time
        // Also Save the private key so it is not generated each and every time
        val privateKey = kp.private
        val privateKeyBytes = privateKey.encoded
        this.privateKey = String(Base64.encode(privateKeyBytes, Base64.DEFAULT))
    }

    fun encrypt(clearText: String): String {

        var encryptedBase64 = ""
        try {

            val factory: KeyFactory = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) KeyFactory.getInstance(CRYPTO_METHOD)
            else KeyFactory.getInstance(CRYPTO_METHOD, "BC")

            val keySpec = X509EncodedKeySpec(Base64.decode(publicKey.trim { it <= ' ' }.toByteArray(), Base64.DEFAULT))

            val key = factory.generatePublic(keySpec)

            val cipher = Cipher.getInstance(CYPHER)
            cipher.init(Cipher.ENCRYPT_MODE, key)

            val encryptedBytes = cipher.doFinal(clearText.toByteArray(charset(CHARSET)))

            encryptedBase64 = String(Base64.encode(encryptedBytes, Base64.DEFAULT))

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return encryptedBase64.replace("(\\r|\\n)".toRegex(), "")
    }

    fun decrypt(encryptedBase64: String): String {
        var decryptedString = ""
        try {
            val keyFac = KeyFactory.getInstance(CRYPTO_METHOD)
            val keySpec = PKCS8EncodedKeySpec(
                    Base64.decode(privateKey.trim { it <= ' ' }.toByteArray(), Base64.DEFAULT))
            val key = keyFac.generatePrivate(keySpec)
            val cipher = Cipher.getInstance(CYPHER)
            cipher.init(Cipher.DECRYPT_MODE, key)
            val encryptedBytes = Base64.decode(encryptedBase64, Base64.DEFAULT)
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            decryptedString = String(decryptedBytes)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return decryptedString
    }

    private fun createPublicKey(): PublicKey {

        val mod = "ofmSMrbxRpe8JuhXNmjqc5Byuulm82MqCSav0CI7PsIQWWj0k661WjmsKhVkoIxMY9D6tt43i9IZDryd2" +
                "NaH4tHdP6fg7jlx/rhb18WdiwFv4t6Z89+0S+EoSL9pp1+yOUqVCH1+3GoD0RP3Iha232nerJbpFcgdLYx4" +
                "HugVT8KBVoPZbvxMpLBFlNy2SnnZ55sUXivw6Vw7pwpUWRC5U8GjFy7CUiytHyDLn0zEVGFnGML2xxFx3Ro" +
                "2CcvntehXZ2RcvGzYes0CiMA0fmpDu1Ov73UfzusU5uFH5ZMq0AvGh9DmxD9BSiV7I93/fd9e5h+pM2W57v" +
                "vXdwhuSLtFDQ=="

        val exp = "AQAB"

        val modules = BigInteger(1, Base64.decode(mod, Base64.NO_WRAP))

        val exponent = BigInteger(1, Base64.decode(exp, Base64.NO_WRAP))

        val factory: KeyFactory = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) KeyFactory.getInstance(CRYPTO_METHOD)
        else KeyFactory.getInstance(CRYPTO_METHOD, "BC")

        return factory.generatePublic(RSAPublicKeySpec(modules, exponent))
    }

    private fun reverse(num: ByteArray): ByteArray {
        return num.reversedArray()
    }

    private fun encrypt(input: String, publicKey: PublicKey): String {
        val stringBuilder = StringBuilder()
        try {
            val keySize = 2048 / 8
            val bytes = input.toByteArray(charset("UTF-32LE"))
            val maxLength = keySize - 42
            val dataLength = bytes.size
            val iterations = dataLength / maxLength
            val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding ", "BC") // 4.4
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            for (i in 0..iterations) {
                val indexBefore = dataLength - maxLength * i
                var index = maxLength
                if (indexBefore < maxLength)
                    index = indexBefore
                val offset = maxLength * i
                val tempBytes = ByteArray(index)
                System.arraycopy(bytes, offset, tempBytes, 0, tempBytes.size - 1)
                val encryptedBytes = cipher.doFinal(tempBytes)
                val encryptedBytes1 = reverse(encryptedBytes)
                stringBuilder.append(Base64.encodeToString(encryptedBytes1, Base64.NO_WRAP))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return stringBuilder.toString()
    }

    fun createRSA(id: String, password: String): String {
        val publicKey = createPublicKey()
        val tokenJson = "{\n \"userName\" : \"$id\",\n \"passWord\" : \"$password\"\n}"
        return encrypt(tokenJson, publicKey).replace("\\+".toRegex(), "_")
    }

}

