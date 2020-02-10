package com.huy.library.handler

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class RepeatThread : BaseHandlerThread {

    private var generator: Runnable

    private val delayInterval: Long

    constructor(name: String, delay: Long, receiver: DataReceiver) : super(name, receiver) {
        delayInterval = delay
        generator = object : Runnable {
            override fun run() {
                onDataGenerate()?.also {
                    val msg = dataHandler?.obtainMessage()
                    msg?.obj = it
                    msg?.sendToTarget()
                }
                dataHandler?.postDelayed(this, delay)
            }
        }
    }

    override fun playGenerate() {
        if (generating) return
        generating = true
        dataHandler?.apply { postDelayed(generator, delayInterval) }
    }

    override fun pauseGenerate() {
        if (!generating) return
        generating = false
        dataHandler?.apply { removeCallbacks(generator) }
    }


}