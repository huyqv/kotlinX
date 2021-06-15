package com.kotlin.app

import android.app.Activity
import android.app.Application
import com.example.library.Library
import com.example.library.extension.SimpleActivityLifecycleCallbacks
import com.example.library.util.SharedPref

import template.data.db.RoomDB
import template.data.network.ApiClient
import template.data.network.ApiService
import java.lang.ref.WeakReference

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        app = this
        Library.init(this)
        registerActivityLifecycleCallbacks(object : SimpleActivityLifecycleCallbacks {
            override fun onActivityResumed(activity: Activity) {
                activityReference = WeakReference(activity)
            }
        })
    }
}

lateinit var app: App private set

private var activityReference: WeakReference<Activity>? = null

val currentActivity: Activity? get() = activityReference?.get()

val pref: SharedPref by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { SharedPref(appId) }

val room: RoomDB by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { RoomDB.getInstance(app, appId) }

val apiClient: ApiClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ApiClient() }

val apiService: ApiService get() = apiClient.service

val isDebug get() = BuildConfig.DEBUG

const val appId = BuildConfig.APPLICATION_ID

const val dbVersion = BuildConfig.DATABASE_VERSION

const val serviceUrl = BuildConfig.SERVICE_URL

val flavor get() = BuildConfig.FLAVOR

val versionCode get() = BuildConfig.VERSION_CODE

val versionName get() = BuildConfig.VERSION_NAME

