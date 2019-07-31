package com.huy.library.handler

import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference

class DataHandler<T> : Handler() {

    private var dataReceivedRef: WeakReference<DataReceiver<T>>? = null

    fun attach(dataReceiver: DataReceiver<T>?) {
        if (dataReceiver != null) {
            dataReceivedRef = WeakReference(dataReceiver)
        }
    }

    override fun handleMessage(msg: Message?) {
        @Suppress("UNCHECKED_CAST") val data = msg?.obj as? T ?: return
        dataReceivedRef?.get()?.also { command -> command.onDataReceived(data) }
    }
}
