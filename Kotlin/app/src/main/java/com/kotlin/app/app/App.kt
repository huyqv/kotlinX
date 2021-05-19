package com.kotlin.app.app

import android.app.Application
import com.example.library.Library

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/11/24
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        Library.app = this
    }

    companion object {
        lateinit var instance: App private set
    }
}