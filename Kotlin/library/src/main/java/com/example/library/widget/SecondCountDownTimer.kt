package com.example.library.widget

import android.os.CountDownTimer

abstract class SecondCountDownTimer(secondFuture: Int, secondInterval: Int)
    : CountDownTimer(secondFuture * 1000L, secondInterval * 1000L) {

    var isRunning: Boolean = false
        private set

    final override fun onFinish() {
        isRunning = false
        onFinished()
    }

    final override fun onTick(millisUntilFinished: Long) {
        onTicks(millisUntilFinished / 1000)
    }

    open fun onFinished() = Unit

    open fun onTicks(second: Long) = Unit

    fun starts() {
        isRunning = true
        super.start()
    }

    fun cancels() {
        isRunning = true
        super.cancel()
    }
}