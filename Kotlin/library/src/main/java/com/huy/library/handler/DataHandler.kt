package com.huy.library.handler

import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class DataHandler<T> : Handler() {

    private var dataReceivedRef: WeakReference<DataReceiver<T>>? = null

    fun attach(dataReceiver: DataReceiver<T>?) {
        if (dataReceiver != null) {
            dataReceivedRef = WeakReference(dataReceiver)
        }
    }

    override fun handleMessage(msg: Message?) {
        @Suppress("UNCHECKED_CAST") val data = msg?.obj as? T ?: return
        dataReceivedRef?.get()?.also { receiver -> receiver.onDataReceived(data) }
    }
}
