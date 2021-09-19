package com.sample.library.extension

import android.app.Activity
import android.app.Application
import android.os.Bundle

interface SimpleActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) = Unit
    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityResumed(activity: Activity) = Unit
    override fun onActivityPaused(activity: Activity) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
}


