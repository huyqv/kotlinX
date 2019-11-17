package com.huy.library.util.network

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.annotation.NonNull
import androidx.annotation.RequiresPermission
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/2
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
@SuppressLint("SupportAnnotationUsage")
object NetworkUtil : BroadcastReceiver() {

    @RequiresPermission(allOf = [(Manifest.permission.ACCESS_NETWORK_STATE), (Manifest.permission.ACCESS_WIFI_STATE)])
    fun isConnected(@NonNull context: Context): Boolean {

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    /** Required permission:
     * -------------------------------------------------------------------------------------------
     *
     *   <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
     *   ...
     *   <receiver android:name=".network.NetworkUtil"
     *       android:enabled="true"
     *       android:exported="false">
     *       <intent-filter>
     *          <action android:name="android.net.conn.CONNECTIVITY_CHANGE"/>
     *          <action android:name="android.intent.action.BOOT_COMPLETED"/>
     *          <action android:name="android.intent.action.QUICKBOOT_POWERON" />
     *          <action android:name="android.net.wifi.WIFI_STATE_CHANGED"/>
     *      </intent-filter>
     *   </receiver>
     *
     * ------------------------------------------------------------------------------------------*/
    @RequiresPermission(anyOf = [Manifest.permission.INTERNET, Manifest.permission.RECEIVE_BOOT_COMPLETED, Manifest.permission.ACCESS_NETWORK_STATE])
    override fun onReceive(context: Context, intent: Intent) {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.detailedState == NetworkInfo.DetailedState.CONNECTED) {

        } else if (networkInfo != null) {

        } else {

        }
    }

    fun isNetworkException(e: Throwable): Boolean {
        return e is SocketException || e is UnknownHostException || e is SocketTimeoutException
    }


}