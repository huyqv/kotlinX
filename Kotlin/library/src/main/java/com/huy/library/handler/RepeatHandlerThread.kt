package com.huy.library.handler

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class RepeatHandlerThread<T> : BaseHandlerThread<T> {

    @Volatile
    private var generating = false

    private val generator: Runnable

    private val delayInterval: Long

    protected abstract fun onDataGenerate(): T

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

    open fun playGenerate() {
        if (generating) return
        handler?.apply {
            generating = true
            postDelayed(generator, delayInterval)
        }
    }

    open fun pauseGenerate() {
        if (!generating) return
        handler?.apply {
            generating = false
            removeCallbacks(generator)
        }
    }

    fun isGenerating(): Boolean {
        return generating
    }

}