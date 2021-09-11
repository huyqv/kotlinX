package com.sample.library.usb

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbEndpoint
import android.hardware.usb.UsbInterface

class UsbDriver {

    var usbInterface: UsbInterface? = null

    var connection: UsbDeviceConnection? = null

    var endpointIn: UsbEndpoint? = null

    var endpointOut: UsbEndpoint? = null

    var usbDevice: UsbDevice? = null

    fun open(device: UsbDevice): Boolean {

        usbDevice = device

        val interfaceCount = device.interfaceCount

        if (interfaceCount == 0) return false

        usbInterface = device.getInterface(0)

        val endpointCount = usbInterface!!.endpointCount

        if (endpointCount > 0) endpointIn = usbInterface!!.getEndpoint(0)

        if (endpointCount > 1) endpointOut = usbInterface!!.getEndpoint(1)

        connection = Usb.manager.openDevice(device)
        return when {
            connection == null -> false
            connection?.claimInterface(usbInterface, true) == true -> true
            else -> {
                connection?.close()
                false
            }
        }
    }

    fun close() {
        try {
            if (null != usbInterface) connection?.releaseInterface(usbInterface)
            usbInterface = null
            connection = null
        } catch (e: Exception) {
        }
    }
}

