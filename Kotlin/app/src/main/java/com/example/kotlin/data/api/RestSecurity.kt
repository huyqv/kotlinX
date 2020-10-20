package com.example.kotlin.data.api

import android.net.http.X509TrustManagerExtensions
import android.util.Base64
import okhttp3.OkHttpClient
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.Certificate
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*
import javax.security.cert.CertificateException

object RestSecurity {

    private val trustManager: X509TrustManager = object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()

        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }
    }

    private val sslContext: SSLContext
        get() = SSLContext.getInstance("SSL").also {
            it.init(null, arrayOf(trustManager), SecureRandom())
        }

    @Throws(SSLException::class)
    private fun validatePinning(trustManagerExt: X509TrustManagerExtensions, conn: HttpsURLConnection, validPins: Set<String>) {
        var certChainMsg = ""
        try {
            val md: MessageDigest = MessageDigest.getInstance("SHA-256")
            val trustedChain = trustedChain(trustManagerExt, conn)
            for (cert in trustedChain) {
                val publicKey = cert.publicKey.encoded
                md.update(publicKey, 0, publicKey.size)
                val pin: String = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                certChainMsg += "sha256/$pin : ${cert.subjectDN}"
                if (validPins.contains(pin)) {
                    return
                }
            }
        } catch (e: NoSuchAlgorithmException) {
            throw SSLException(e)
        }
        throw SSLPeerUnverifiedException("Peer certificate chain:$certChainMsg")
    }

    @Throws(SSLException::class)
    private fun trustedChain(trustManagerExt: X509TrustManagerExtensions, conn: HttpsURLConnection): List<X509Certificate> {
        val serverCerts: Array<out Certificate> = conn.serverCertificates
        val untrustedCerts: Array<X509Certificate> = Arrays.copyOf(serverCerts, serverCerts.size, Array<X509Certificate>::class.java)
        val host: String = conn.url.host
        return try {
            trustManagerExt.checkServerTrusted(untrustedCerts, "RSA", host)
        } catch (e: CertificateException) {
            throw SSLException(e)
        }
    }

    @Throws(NoSuchAlgorithmException::class)
    fun String.sha256(): String? {
        val digest = MessageDigest.getInstance("SHA-256").digest(this.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(digest, Base64.NO_WRAP)
    }

    fun trustClient(client: OkHttpClient.Builder) {
        client.sslSocketFactory(sslContext.socketFactory, trustManager)
                .hostnameVerifier(HostnameVerifier { _, _ -> true })
    }

}