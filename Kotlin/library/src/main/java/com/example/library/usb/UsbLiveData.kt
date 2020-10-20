package com.example.library.usb

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class UsbLiveData private constructor() : MutableLiveData<MutableMap<Int, UsbEvent>>() {

    companion object {

        val instance: UsbLiveData by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            UsbLiveData()
        }

        fun builder(activity: AppCompatActivity): Builder {
            return Builder(activity)
        }

        fun postValue(vendorId: Int, usbEvent: UsbEvent) {
            val map = instance.value?.also {
                it[vendorId] = usbEvent
            }
            instance.postValue(map)
        }
    }

    class Builder(val activity: AppCompatActivity) {

        private lateinit var vendorIds: IntArray

        fun devices(vararg vendorIds: Int): Builder {
            this.vendorIds = vendorIds
            return this
        }

        fun observe(block: (Map<Int, UsbEvent>) -> Unit) {
            instance.observe(activity, Observer {
                if (null != it) {
                    block(it)
                }
            })
            Usb.observer(activity, *vendorIds)
        }
    }

}