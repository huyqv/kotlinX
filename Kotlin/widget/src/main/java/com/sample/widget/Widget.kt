package com.sample.widget

import android.app.Application

object Widget {

    fun init(application: Application) {
        mApp = application
    }
}

private var mApp: Application? = null

val app: Application
    get() = mApp ?: throw NullPointerException(
        "Library module must be init with " +
                "Widget.init(application: Application) in android.app.Application.onCreate()"
    )