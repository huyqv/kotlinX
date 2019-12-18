package com.huy.library

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

    private var enable: Boolean = false

    constructor(string: String, enable: Boolean) {
        this.tag = if (string.length > 23) string.substring(0, 22) else string
        this.enable = enable
    }

    constructor(cls: Class<*>, enable: Boolean) : this(cls.simpleName, enable)

    fun json(s: String?) {
        val json = if (s.isNullOrEmpty()) null else try {
            JSONObject(s).toString(2)
        } catch (e: JSONException) {
            null
        }
        i(json)
    }

    fun d(string: String?) {
        if (enable && !string.isNullOrEmpty()) Log.d(tag, string)
    }

    fun d(throwable: Throwable?) {
        d(throwable?.message)
    }

    fun i(string: String?) {
        if (enable && !string.isNullOrEmpty()) Log.i(tag, string)
    }

    fun i(throwable: Throwable?) {
        i(throwable?.message)
    }

    fun e(string: String?) {
        if (enable && string.isNullOrEmpty()) Log.e(tag, string)
    }

    fun e(throwable: Throwable?) {
        e(throwable?.message)
    }

    fun w(string: String?) {
        if (enable && !string.isNullOrEmpty()) Log.w(tag, string)
    }

    fun w(throwable: Throwable?) {
        w(throwable?.message)
    }

    fun wtf(string: String?) {
        if (enable && !string.isNullOrEmpty()) Log.wtf(tag, string)
    }

    fun wtf(throwable: Throwable?) {
        wtf(throwable?.message)
    }

    fun breakpoint() {}

}