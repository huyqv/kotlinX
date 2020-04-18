package com.huy.library.thread

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class RepeatThread : BaseHandlerThread {

    open val generator: Runnable = Runnable {
        onDataGenerate()?.also {
            val msg = dataHandler?.obtainMessage()
            msg?.obj = it
            msg?.sendToTarget()
        }
        dataHandler?.postDelayed(this, delayInterval)
    }

    private var delayInterval: Long = Long.MAX_VALUE

    constructor(name: String, delayInterval: Long) : super(name) {
        this.delayInterval = delayInterval
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