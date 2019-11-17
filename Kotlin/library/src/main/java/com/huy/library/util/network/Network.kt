package com.huy.library.util.network

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.huy.library.Library

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/08/13
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class Network {

    class Callback : ConnectivityManager.NetworkCallback() {

        private val connectivityManager: ConnectivityManager
            get() = Library.app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        private val request: NetworkRequest
            get() = NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .build()

        @SuppressLint("MissingPermission")
        fun register() {
            connectivityManager.registerNetworkCallback(request, this)
        }

        fun unregister() {
            connectivityManager.unregisterNetworkCallback(this)
        }

        override fun onAvailable(network: Network) {

        }

        override fun onUnavailable() {

        }
    }

    @Suppress("DEPRECATION")
    class Receiver : BroadcastReceiver() {

        fun register() {
            Library.app.registerReceiver(this, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }

        override fun onReceive(context: Context, intent: Intent) {
        }
    }
}