package com.huy.kotlin.extension

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.huy.kotlin.app.App

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/08/07
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
val networkConnected: Boolean
    get() {
        val cm = App.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        when {

            Build.VERSION.SDK_INT > Build.VERSION_CODES.O -> cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                return when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }

            Build.VERSION.SDK_INT > Build.VERSION_CODES.M -> @Suppress("DEPRECATION")
            cm.activeNetworkInfo?.run {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    return true
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    return true
                }
            }

            else -> @Suppress("DEPRECATION") {
                val activeNetworkInfo = cm.activeNetworkInfo
                return activeNetworkInfo != null && activeNetworkInfo.isConnected
            }
        }
        return false
    }

val networkDisconnected: Boolean get() = !networkConnected