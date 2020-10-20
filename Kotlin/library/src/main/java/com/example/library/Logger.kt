package com.example.library

import android.util.Log
import org.json.JSONException
import org.json.JSONObject

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/18
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
open class Logger {

    private val tag: String

    private val enable: Boolean

    constructor(enable: Boolean = BuildConfig.DEBUG) : this("", enable)

    constructor(string: String, enable: Boolean = BuildConfig.DEBUG) {
        this.tag = if (string.length > 23) string.substring(0, 22) else string
        this.enable = enable
    }

    constructor(cls: Class<*>, enable: Boolean = BuildConfig.DEBUG) : this(cls.simpleName, enable)

    constructor(any: Any, enable: Boolean = BuildConfig.DEBUG) : this(any.javaClass, enable)

    fun json(s: String?) {
        val json = if (s.isNullOrEmpty()) null else try {
            JSONObject(s).toString(2)
        } catch (e: JSONException) {
            null
        }
        i(json)
    }

    fun d(any: Any?, vararg arg: Any?) {
        if (enable) Log.d(tag, String.format(any?.toString() ?: "", *arg))
    }

    fun d(throwable: Throwable?) {
        d(throwable?.message)
    }

    fun i(any: Any?, vararg arg: Any?) {
        if (enable) Log.i(tag, String.format(any?.toString() ?: "", *arg))
    }

    fun i(throwable: Throwable?) {
        i(throwable?.message)
    }

    fun e(any: Any?, vararg arg: Any?) {
        if (enable) Log.e(tag, String.format(any?.toString() ?: "", *arg))
    }

    fun e(throwable: Throwable?) {
        e(throwable?.message)
    }

    fun w(any: Any?, vararg arg: Any?) {
        if (enable) Log.w(tag, String.format(any?.toString() ?: "", *arg))
    }

    fun w(throwable: Throwable?) {
        w(throwable?.message)
    }

    fun wtf(any: Any?, vararg arg: Any?) {
        if (enable) Log.wtf(tag, String.format(any?.toString() ?: "", *arg))
    }

    fun wtf(throwable: Throwable?) {
        wtf(throwable?.message)
    }

    companion object {

        fun breakpoint() {}

        fun crash() {
            arrayOf(true)[-1]
        }

    }

}