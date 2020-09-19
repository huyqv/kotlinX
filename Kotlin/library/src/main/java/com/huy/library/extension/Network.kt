package com.huy.library.extension

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.huy.library.Library

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/08/07
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
private val app: Application get() = Library.app

val connectionInfo: String?
    @SuppressLint("MissingPermission")
    get() {
        val cm = connectivityManager
        when {
            Build.VERSION.SDK_INT > Build.VERSION_CODES.O -> cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                return when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "wifi"
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "cellular"
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "ethernet"
                    else -> null
                }
            }
            Build.VERSION.SDK_INT > Build.VERSION_CODES.M -> @Suppress("DEPRECATION") cm.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> "wifi"
                    ConnectivityManager.TYPE_MOBILE -> "mobile"
                    else -> null
                }
            }
            else -> @Suppress("DEPRECATION") {
                if (cm.activeNetworkInfo?.isConnected == true) {
                    return "is connected"
                }
            }
        }
        return null
    }

val networkConnected: Boolean get() = connectionInfo != null

val networkDisconnected: Boolean get() = connectionInfo == null

val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


