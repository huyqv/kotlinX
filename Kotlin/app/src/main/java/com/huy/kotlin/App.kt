package com.huy.kotlin

import android.app.Application
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

    companion object {

        lateinit var instance: App private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        Library.app = this

        NetworkLiveData.registerCallback()
    }

}