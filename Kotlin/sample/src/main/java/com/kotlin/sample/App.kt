package com.kotlin.sample

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.example.library.Library
import com.example.library.extension.SimpleActivityLifecycleCallbacks
import com.example.library.util.SharedPref
import com.kotlin.sample.data.db.RoomDB
import com.kotlin.sample.data.network.ApiClient
import com.kotlin.sample.data.network.ApiService
import java.lang.ref.WeakReference

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
        registerActivityLifecycleCallbacks(object : SimpleActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activityReference = WeakReference(activity)
            }

            override fun onActivityDestroyed(activity: Activity) {
                activityReference?.clear()
            }

        })
    }
}

lateinit var app: App private set

private var activityReference: WeakReference<Activity>? = null

val appId: String get() = "aa"//BuildConfig.APPLICATION_ID

val currentActivity: Activity? get() = activityReference?.get()

val pref: SharedPref by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { SharedPref(appId) }

val room: RoomDB by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { RoomDB.getInstance(app, appId) }

val apiClient: ApiClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ApiClient() }

val apiService: ApiService get() = apiClient.service
