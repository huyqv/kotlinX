package com.huy.library.handler

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class DelayThread : BaseHandlerThread {

    private val generator: Runnable

    private var delayTime: Long

    constructor(name: String, interval: Long, receiver: DataReceiver) : super(name, receiver) {
        delayTime = interval
        generator = Runnable {
            onDataGenerate()?.also {
                val msg = dataHandler?.obtainMessage()
                msg?.obj = it
                msg?.sendToTarget()
            }
        }
    }

    override fun playGenerate() {
        dataHandler?.apply {
            removeCallbacks(generator)
            postDelayed(generator, delayTime)
        }
    }

    override fun pauseGenerate() {
        dataHandler?.removeCallbacks(generator)
    }

}