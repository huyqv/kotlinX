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
                    val msg = handler?.obtainMessage()
                    msg?.obj = it
                    msg?.sendToTarget()
                }
                handler?.postDelayed(this, delay)
            }
        }
    }

    override fun playGenerate() {
        if (generating) return
        generating = true
        handler?.apply { postDelayed(generator, delayInterval) }
    }

    override fun pauseGenerate() {
        if (!generating) return
        generating = false
        handler?.apply { removeCallbacks(generator) }
    }


}