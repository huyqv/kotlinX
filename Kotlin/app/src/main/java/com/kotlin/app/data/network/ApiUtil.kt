package com.kotlin.app.data.network

import android.net.http.X509TrustManagerExtensions
import android.os.Build
import android.os.Environment
import android.util.Base64
import com.google.gson.JsonObject
import com.kotlin.app.app
import com.kotlin.app.shared.isDebug
import com.sample.library.extension.parse
import com.sample.library.extension.tryFlow
import kotlinx.coroutines.flow.Flow
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.buffer
import okio.sink
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.net.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.Certificate
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import javax.security.cert.CertificateException
import kotlin.reflect.KClass

val loggingInterceptor: Interceptor
    get() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (isDebug) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
        return interceptor
    }

fun initClient(block: (OkHttpClient.Builder.() -> Unit)? = null): OkHttpClient {
    val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
    if (isDebug) {
        client.addInterceptor(loggingInterceptor)
    }
    block?.invoke(client)
    return client.build()
}

fun initRetrofit(baseURL: String, block: (OkHttpClient.Builder.() -> Unit)? = null): Retrofit {
    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(initClient(block))
        .baseUrl(baseURL)
        .build()
}

fun <T : Any> initService(
    cls: KClass<T>,
    url: String,
    block: (OkHttpClient.Builder.() -> Unit)? = null
): T {
    return initRetrofit(url, block).create(cls.java)
}

fun authInterceptor(token: String): Interceptor {
    return Interceptor { chain: Interceptor.Chain ->
        val request = chain.request().newBuilder()
        request.addHeader("Authorization", token)
        chain.proceed(request.build())
    }
}

/**
 * Network security utils
 */
val trustManager: X509TrustManager = object : X509TrustManager {
    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()

    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
    }

    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
    }
}

val sslContext: SSLContext
    get() = SSLContext.getInstance("SSL").also {
        it.init(null, arrayOf(trustManager), SecureRandom())
    }

@Throws(SSLException::class)
fun validatePinning(
    x509Extensions: X509TrustManagerExtensions,
    connection: HttpsURLConnection,
    validPins: Set<String>
) {
    var certChainMsg = ""
    try {
        val md: MessageDigest = MessageDigest.getInstance("SHA-256")
        val trustedChain = trustedChain(x509Extensions, connection)
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
fun trustedChain(
    trustManagerExt: X509TrustManagerExtensions,
    connection: HttpsURLConnection
): List<X509Certificate> {
    val serverCerts: Array<out Certificate> = connection.serverCertificates
    val untrustedCerts: Array<X509Certificate> =
        Arrays.copyOf(serverCerts, serverCerts.size, Array<X509Certificate>::class.java)
    val host: String = connection.url.host
    return try {
        trustManagerExt.checkServerTrusted(untrustedCerts, "RSA", host)
    } catch (e: CertificateException) {
        throw SSLException(e)
    }
}

@Throws(NoSuchAlgorithmException::class)
fun String.sha256(): String? {
    val digest = MessageDigest.getInstance("SHA-256").digest(this.toByteArray(Charsets.UTF_8))
    return Base64.encodeToString(digest, Base64.DEFAULT)
}

fun trustClient(client: OkHttpClient.Builder) {
    client.sslSocketFactory(sslContext.socketFactory, trustManager)
        .hostnameVerifier(HostnameVerifier { _, _ -> true })
}

/**
 * Response utils
 */
val HttpException.errorBody: JsonObject
    get() {
        val errorBody = response()?.errorBody()?.string().parse(JsonObject::class)
        if (null != errorBody) return errorBody
        return JsonObject().also {
            it.addProperty("code", 1)
            it.addProperty("message", this.message())
            it.addProperty("exception", this::class.simpleName)
        }
    }

val Exception.errorBody: JsonObject
    get() {
        return (this as? HttpException)?.errorBody ?: JsonObject().also {
            it.addProperty("code", 1)
            it.addProperty("message", this.message)
            it.addProperty("exception", this::class.simpleName)
        }
    }

fun Throwable.isNetworkError(): Boolean {
    return this is SocketException || this is UnknownHostException || this is SocketTimeoutException
}

fun writeFile(response: Response<ResponseBody>, fileName: String): Flow<Result<File>> {
    return tryFlow {
        val source = response.body()?.source()
            ?: throw NullPointerException("download data is empty")
        val file = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File(
                app.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath,
                fileName
            )
        } else {
            @Suppress("DEPRECATION")
            (File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                fileName
            ))
        }
        file.sink().buffer().apply {
            writeAll(source)
            close()
        }
        return@tryFlow file
    }
}

/**
 *
 */
fun domainName(url: String): String {
    return try {
        val uri = URI(url)
        val domain = uri.host ?: return ""
        if (domain.startsWith("www.")) domain.substring(4) else domain
    } catch (e: URISyntaxException) {
        ""
    }
}

val JSON: MediaType = "application/json; charset=utf-8".toMediaType()