package com.huy.library.handler

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
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
abstract class BaseGenerator<T> : HandlerThread {

    @Volatile
    var isPlaying = false

    @Volatile
    var generating = false

    var handler: Handler? = null

    private val uiHandler = DataHandler<T>()

    protected abstract fun playGenerate()

    protected abstract fun pauseGenerate()

    abstract fun onDataGenerate(): T

    constructor(name: String, receiver: DataReceiver<T>) : super(name) {
        uiHandler.attach(receiver)
    }

    override fun onLooperPrepared() {
        super.onLooperPrepared()
        handler = getHandler(looper)
    }

    fun isGenerating(): Boolean {
        return generating
    }

    fun play() {
        isPlaying = true
        playGenerate()
    }

    fun pause() {
        isPlaying = false
        pauseGenerate()
    }

    fun silentPause() {
        pauseGenerate()
    }

    private fun getHandler(looper: Looper): Handler {
        return object : Handler(looper) {
            override fun handleMessage(msg: Message?) {
                val message = uiHandler.obtainMessage()
                message.obj = msg?.obj
                message.sendToTarget()
            }
        }
    }

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

}