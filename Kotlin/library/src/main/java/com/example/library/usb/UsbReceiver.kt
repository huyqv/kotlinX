package com.example.library.usb

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Parcelable

open class UsbReceiver(private val vendorIdList: IntArray) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val usb = intent?.getParcelableExtra<Parcelable>(UsbManager.EXTRA_DEVICE) as? UsbDevice
                ?: return
        if (vendorIdList.indexOf(usb.vendorId) == -1) return
        UsbLiveData.instance.value = usb
    }
}