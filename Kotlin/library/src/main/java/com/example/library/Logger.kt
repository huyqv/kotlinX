package com.example.library

import android.util.Log
import com.google.gson.JsonObject
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

    constructor(string: String) {
        this.tag = if (string.length > 23) string.substring(0, 22) else string
    }

    constructor(cls: Class<*>) : this(cls.simpleName)

    constructor(any: Any) : this(any.javaClass)

    constructor() : this("")

    fun d(s: JsonObject) {
        val json = s.toString()
        d(JSONObject(json).toString(2))
    }

    fun d(any: Any?) {
        if (enable) Log.d(tag, any.toString())
    }

    fun d(throwable: Throwable?) {
        d(throwable?.message)
    }

    fun i(any: Any?) {
        if (enable) Log.i(tag, any.toString())
    }

    fun i(throwable: Throwable?) {
        i(throwable?.message)
    }

    fun e(any: Any?) {
        if (enable) Log.e(tag, any.toString())
    }

    fun e(throwable: Throwable?) {
        e(throwable?.message)
    }

    fun w(any: Any?) {
        if (enable) Log.w(tag, any.toString())
    }

    fun w(throwable: Throwable?) {
        w(throwable?.message)
    }

    fun wtf(any: Any?) {
        if (enable) Log.wtf(tag, any.toString())
    }

    fun wtf(throwable: Throwable?) {
        wtf(throwable?.message)
    }

    companion object {

        var enable: Boolean = true

        fun breakpoint() {}

        fun error() {
            arrayOf(true)[-1]
        }

        fun crash() {
            throw RuntimeException("crash")
        }

    }

}