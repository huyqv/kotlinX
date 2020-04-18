package com.huy.library.thread

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class DelayThread constructor(name: String, private val interval: Long) : BaseHandlerThread(name) {

    open val generator: Runnable = Runnable {
        onDataGenerate()?.also {
            val msg = dataHandler?.obtainMessage()
            msg?.obj = it
            msg?.sendToTarget()
        }
    }


    override fun playGenerate() {
        dataHandler?.apply {
            removeCallbacks(generator)
            postDelayed(generator, interval)
        }
    }

    override fun pauseGenerate() {
        dataHandler?.removeCallbacks(generator)
    }

}