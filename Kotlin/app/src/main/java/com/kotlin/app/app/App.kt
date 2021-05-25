package com.kotlin.app.app

import android.app.Application
import com.example.library.Library
import com.example.library.util.SharedPref
import com.kotlin.app.BuildConfig
import com.kotlin.app.data.db.RoomDB
import com.kotlin.app.data.network.ApiClient
import com.kotlin.app.data.network.ApiService

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
        app = this
        Library.app = this
    }
}

lateinit var app: App private set

val appId: String get() = BuildConfig.APPLICATION_ID

val pref: SharedPref by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { SharedPref(appId) }

val room: RoomDB by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { RoomDB.getInstance(app, appId) }

val apiClient: ApiClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ApiClient() }

val apiService: ApiService get() = apiClient.service

