package com.kotlin.app

import android.app.Activity
import android.app.Application
import com.example.library.Library
import com.example.library.extension.SimpleActivityLifecycleCallbacks
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



