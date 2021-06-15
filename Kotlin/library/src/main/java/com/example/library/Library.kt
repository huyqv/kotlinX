package com.example.library

import android.app.Activity
import android.app.Application
import com.example.library.extension.SimpleActivityLifecycleCallbacks
import java.lang.ref.WeakReference

object Library {

    fun init(application: Application) {
        mApp = application
        app.registerActivityLifecycleCallbacks(object : SimpleActivityLifecycleCallbacks {
            override fun onActivityResumed(activity: Activity) {
                activityReference = WeakReference(activity)
            }
        })
    }
}

private var mApp: Application? = null

val app: Application
    get() = mApp ?: throw NullPointerException("Library module must be init with " +
            "Library.init(application: Application) in android.app.Application.onCreate()")

private var activityReference: WeakReference<Activity>? = null

val currentActivity: Activity? get() = activityReference?.get()
