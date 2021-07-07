package com.kotlin.app.shared

import com.kotlin.app.BuildConfig
import com.kotlin.app.app
import com.kotlin.app.data.db.RoomDB
import com.kotlin.app.data.network.ApiClient
import com.kotlin.app.data.network.ApiService
import com.sample.library.util.SharedPref

const val appId = BuildConfig.APPLICATION_ID

const val dbVersion = BuildConfig.DATABASE_VERSION

const val serviceUrl = BuildConfig.SERVICE_URL

val isDebug get() = BuildConfig.DEBUG

val flavor get() = BuildConfig.FLAVOR

val versionCode get() = BuildConfig.VERSION_CODE

val versionName get() = BuildConfig.VERSION_NAME

val pref: SharedPref by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { SharedPref(appId) }

val room: RoomDB by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { RoomDB.getInstance(app, appId) }

val apiClient: ApiClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ApiClient() }

val apiService: ApiService get() = apiClient.service