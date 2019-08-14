package com.huy.kotlin.app

import android.app.Application
import android.content.Context
import com.huy.library.Library

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/11/24
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class App : Application(), Library.Extension {

    var isForeground: Boolean = false

    override fun onCreate() {
        super.onCreate()
        Library.inject(this)
        instance = this
    }

    companion object {

        lateinit var instance: App private set

        val appContext: Context get() = instance.applicationContext

        val baseContext: Context get() = instance.baseContext
    }

}