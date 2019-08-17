package com.huy.library.handler

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class DelayGenerator<T> : BaseGenerator<T> {

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

    override fun playGenerate() {
        handler?.apply {
            removeCallbacks(generator)
            postDelayed(generator, delayTime)
        }
    }

    override fun pauseGenerate() {
        handler?.removeCallbacks(generator)
    }

}