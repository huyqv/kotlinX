package com.huy.library.usb

import android.hardware.usb.UsbDevice

class UsbEvent(val status: String, val usb: UsbDevice?) {

    val isAttached: Boolean get() = usb != null

    val hasPermission: Boolean get() = usb != null && Usb.hasPermission(usb)

    override fun toString(): String {
        usb ?: return "device not found"
        return "${usb.productName} $status"
    }

}