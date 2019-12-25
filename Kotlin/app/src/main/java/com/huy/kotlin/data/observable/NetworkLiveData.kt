package com.huy.kotlin.data.observable

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import com.huy.library.extension.connectivityManager


/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/04/19
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class NetworkLiveData private constructor() : SingleLiveData<Boolean?>() {

    companion object {

        val instance: NetworkLiveData by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkLiveData()
        }

        private val networkCallback: ConnectivityManager.NetworkCallback by lazy {
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    instance.postValue(true)
                }

                override fun onLost(network: Network?) {
                    instance.postValue(false)
                }
            }
        }

        fun registerCallback() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager.registerDefaultNetworkCallback(networkCallback)
            } else {
                val request = NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_WIFI_P2P).build()
                connectivityManager.registerNetworkCallback(request, networkCallback)
            }
        }

    }


}

