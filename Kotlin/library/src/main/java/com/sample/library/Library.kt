package com.sample.library

import android.app.Application

object Library {

    fun init(application: Application) {
        mApp = application
    }
}

private var mApp: Application? = null

val app: Application
    get() = mApp ?: throw NullPointerException(
        "Library module must be init with " +
                "Library.init(application: Application) in android.app.Application.onCreate()"
    )