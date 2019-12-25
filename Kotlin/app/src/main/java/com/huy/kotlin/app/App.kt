package com.huy.kotlin.app

import android.app.Application
import android.content.Context
import com.huy.kotlin.data.observable.NetworkLiveData
import com.huy.library.Library

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/11/24
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class App : Application() {

    var isForeground: Boolean = false

    override fun onCreate() {
        super.onCreate()

        instance = this

        Library.app = this

        NetworkLiveData.registerCallback()
    }

    companion object {

        lateinit var instance: App private set

        val appContext: Context get() = instance.applicationContext

        val baseContext: Context get() = instance.baseContext
    }

}