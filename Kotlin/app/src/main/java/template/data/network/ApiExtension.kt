package template.data.network

import android.net.http.X509TrustManagerExtensions
import android.os.Build
import android.os.Environment
import android.util.Base64
import com.example.library.extension.parse
import com.google.gson.JsonObject
import com.kotlin.app.app
import com.kotlin.app.isDebug
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
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
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
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
        interceptor.level = if (isDebug) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return interceptor
    }

fun initClient(block: OkHttpClient.Builder.() -> Unit = {}): OkHttpClient {
    val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
    if (isDebug) {
        client.addInterceptor(loggingInterceptor)
    }
    client.block()
    return client.build()
}

fun initRetrofit(baseURL: String, block: OkHttpClient.Builder.() -> Unit = {}): Retrofit {
    return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(initClient(block))
            .baseUrl(baseURL)
            .build()
}

fun <T : Any> initService(kClass: KClass<T>, baseURL: String, block: OkHttpClient.Builder.() -> Unit = {}): T {
    return initRetrofit(baseURL, block).create(kClass.java)
}

fun OkHttpClient.Builder.addAuthInterceptor(token: String?) {
    addInterceptor(object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val request = chain.request().newBuilder()
            if (token != null) {
                request.addHeader("Authorization", String.format("Bearer %s", token))
            }
            return chain.proceed(request.build())
        }
    })
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
fun validatePinning(trustManagerExt: X509TrustManagerExtensions, conn: HttpsURLConnection, validPins: Set<String>) {
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
fun trustedChain(trustManagerExt: X509TrustManagerExtensions, conn: HttpsURLConnection): List<X509Certificate> {
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

fun Observable<Response<ResponseBody>>.writeFile(fileName: String): Observable<File> {
    return flatMap(object : Function<Response<ResponseBody>, ObservableSource<File>> {
        override fun apply(response: Response<ResponseBody>): ObservableSource<File> {
            return Observable.create(object : ObservableOnSubscribe<File> {
                override fun subscribe(emitter: ObservableEmitter<File>) {
                    val source = response.body()?.source()
                            ?: throw NullPointerException("download data is empty")
                    try {
                        val file = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            File(app.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath, fileName)
                        } else {
                            @Suppress("DEPRECATION")
                            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
                        }
                        file.sink().buffer().apply {
                            writeAll(source)
                            close()
                        }
                        emitter.onNext(file)
                    } catch (e: IOException) {
                        emitter.onError(e)
                    } finally {
                        emitter.onComplete()
                    }
                }
            })
        }
    })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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






