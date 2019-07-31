package com.huy.kotlin.network.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.huy.kotlin.data.observable.NetworkLiveData
import com.huy.kotlin.extension.networkConnected

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/04/19
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class NetworkStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        NetworkLiveData.isConnected = networkConnected
    }
}