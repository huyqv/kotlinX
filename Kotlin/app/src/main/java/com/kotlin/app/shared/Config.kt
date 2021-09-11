package com.kotlin.app.shared

import com.kotlin.app.BuildConfig

const val appId = BuildConfig.APPLICATION_ID

const val dbVersion = BuildConfig.DATABASE_VERSION

const val serviceUrl = BuildConfig.SERVICE_URL

val isDebug get() = BuildConfig.DEBUG

val flavor get() = BuildConfig.FLAVOR

val versionCode get() = BuildConfig.VERSION_CODE

val versionName get() = BuildConfig.VERSION_NAME