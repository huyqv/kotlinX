package com.huy.library.usb

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.huy.library.Library

object Usb {

    const val ERROR: String = "error"

    const val DETACHED: String = "detached"

    const val ATTACHED: String = "attached"

    const val GRANTED: String = "granted"

    const val DENIED: String = "denied"

    const val PERMISSION = ".USB_PERMISSION"

    val manager: UsbManager =
            ContextCompat.getSystemService(Library.app, UsbManager::class.java) as UsbManager

    val intentFilter: IntentFilter = IntentFilter(PERMISSION).also {
        it.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        it.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        it.addAction(UsbManager.EXTRA_PERMISSION_GRANTED)
    }

    val deviceList: List<UsbDevice>
        get() {
            val list = mutableListOf<UsbDevice>()
            val map: HashMap<String, UsbDevice> = manager.deviceList
            map.forEach { list.add(it.value) }
            return list
        }

    fun getDevice(vendorId: Int): UsbDevice? {
        deviceList.forEach { if (vendorId == it.vendorId) return it }
        return null
    }

    fun observer(activity: AppCompatActivity, vararg vendorIds: Int) {
        val receiver = UsbReceiver(vendorIds)
        activity.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() {
                activity.registerReceiver(receiver, intentFilter)
                receiver.findDevice()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                activity.unregisterReceiver(receiver)
            }
        })
    }

    fun requestPermission(usb: UsbDevice) {
        if (!manager.hasPermission(usb)) {
            val intent = PendingIntent.getBroadcast(Library.app, 1234, Intent(PERMISSION), 0)
            manager.requestPermission(usb, intent)
        }
    }

    fun requestPermission(vendorId: Int) {
        getDevice(vendorId)?.also {
            requestPermission(it)
        }
    }

    fun hasPermission(usb: UsbDevice): Boolean {
        return manager.hasPermission(usb)
    }

    fun forceClose(usb: UsbDevice?) {
        usb ?: return
        val connection = manager.openDevice(usb)
        for (i in usb.interfaceCount - 1 downTo 0) {
            connection.releaseInterface(usb.getInterface(i))
        }
        connection.close()
    }

    fun forceClose(vendorId: Int) {
        forceClose(getDevice(vendorId))
    }

    fun deviceStatus(vendorId: Int): UsbEvent {
        val usb = getDevice(vendorId)
        return deviceStatus(usb)
    }

    fun deviceStatus(usb: UsbDevice?): UsbEvent {
        return when {
            null == usb -> UsbEvent(DETACHED, usb)
            hasPermission(usb) -> UsbEvent(GRANTED, usb)
            else -> UsbEvent(ATTACHED, usb)
        }
    }

}