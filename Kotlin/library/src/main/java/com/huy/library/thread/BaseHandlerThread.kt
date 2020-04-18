package com.huy.library.thread

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BaseHandlerThread constructor(name: String) : HandlerThread(name) {

    @Volatile
    var isPlaying = false

    @Volatile
    var generating = false

    var dataHandler: Handler? = null

    var receiver: DataReceiver?
        get() = uiHandler.dataReceiver
        set(value) {
            uiHandler.dataReceiver = value
        }

    private val uiHandler = DataHandler()

    protected abstract fun playGenerate()

    protected abstract fun pauseGenerate()

    abstract fun onDataGenerate(): Any?

    override fun onLooperPrepared() {
        super.onLooperPrepared()
        dataHandler = getHandler(looper)
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

    inner class DataHandler : Handler() {

        var dataReceiver: DataReceiver? = null

        override fun handleMessage(msg: Message?) {
            val data = msg?.obj ?: return
            dataReceiver?.also {
                it.onDataReceived(name, data)
            }
        }
    }

}