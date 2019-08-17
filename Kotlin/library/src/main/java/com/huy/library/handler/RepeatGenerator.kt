package com.huy.library.handler

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class RepeatGenerator<T> : BaseGenerator<T> {

    private val generator: Runnable

    private val delayInterval: Long

    constructor(name: String, delayInterval: Long, receiver: DataReceiver<T>) : super(name, receiver) {
        this.delayInterval = delayInterval
        generator = object : Runnable {
            override fun run() {
                val msg = handler?.obtainMessage() ?: return
                msg.obj = onDataGenerate()
                msg.sendToTarget()
                handler?.postDelayed(this, delayInterval)
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