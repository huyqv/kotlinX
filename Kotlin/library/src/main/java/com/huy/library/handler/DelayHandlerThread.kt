package com.huy.library.handler

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class DelayHandlerThread<T> : BaseHandlerThread<T> {

    protected abstract fun onDataGenerate(): T

    @Volatile
    private var generating = false

    private val generator: Runnable

    private var delayTime: Long

    constructor(name: String, interval: Long, receiver: DataReceiver<T>) : super(name, receiver) {
        delayTime = interval
        generator = object : Runnable {
            override fun run() {
                val msg = handler?.obtainMessage() ?: return
                msg.obj = onDataGenerate()
                msg.sendToTarget()
            }
        }
    }

    open fun playGenerate() {
        handler?.apply {
            removeCallbacks(generator)
            postDelayed(generator, delayTime)
        }
    }

    open fun pauseGenerate() {
        handler?.removeCallbacks(generator)
    }

}