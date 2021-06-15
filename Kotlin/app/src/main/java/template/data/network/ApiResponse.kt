package template.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ApiResponse<T> {

    @SerializedName("code")
    var code: Int = 500

    @SerializedName("message")
    @Expose
    var message: String = "Something went wrong :( !"

    @SerializedName("body")
    @Expose
    var body: T? = null

    constructor(throwable: Throwable?) {

        if (throwable == null) {
            return
        }

        if (throwable.isNetworkError()) {
            message = "Network error !"
            return
        }

        throwable.message?.apply { message = this }
    }

    constructor(response: Response<T>?) {

        response ?: return

        code = response.code()

        if (response.isSuccessful) {
            message = response.message()
            body = response.body()
            return
        }

        response.errorBody()?.string()?.apply {
            message = this
        }

    }

    constructor(status: Int, message: String, body: T?) {
        this.code = status
        this.message = message
        this.body = body
    }

    private fun Throwable.isNetworkError(): Boolean {
        return this is SocketException || this is UnknownHostException || this is SocketTimeoutException
    }

    val isSuccessful: Boolean get() = code in 200..299 && body != null

}