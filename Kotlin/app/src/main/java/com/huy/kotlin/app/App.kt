package com.huy.kotlin.app

import android.app.Application
import android.content.Context

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/11/24
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class App : Application() {

    var isForeground: Boolean = false

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        lateinit var instance: App private set

        val appContext: Context get() = instance.applicationContext

        val baseContext: Context get() = instance.baseContext
    }

}