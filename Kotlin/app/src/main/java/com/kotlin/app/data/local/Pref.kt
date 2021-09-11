package com.kotlin.app.data.local

import com.kotlin.app.shared.appId
import com.sample.library.util.SharedPref

val pref: SharedPref by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { SharedPref(appId) }

var samplePref: String?
    get() = pref.str("sample")
    set(value) = pref.edit("sample", value)