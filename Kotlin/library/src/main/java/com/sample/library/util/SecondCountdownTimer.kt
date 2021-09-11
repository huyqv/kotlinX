package com.sample.library.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.*

class SecondCountdownTimer(private val intervalMillis: Long = 10 * SECOND) {

    companion object {
        private const val SECOND = 1000L
        val now get() = System.currentTimeMillis()
    }

    private var startTime: Long = 0

    private var job: Job? = null

    private val stickLiveData = MutableLiveData<Long>()

    fun start(lifecycleOwner: LifecycleOwner) {
        startTime = now
        stickLiveData.value = intervalMillis + startTime - now
        stickLiveData.observe(lifecycleOwner, Observer {
            it ?: return@Observer
            if (it > 0) {
                val seconds = it / 1000
                onTicks(seconds, "%02d:%02d".format(seconds / 60, seconds % 60))
            } else {
                onFinished()
            }
        })
        resume()
    }

    fun cancel() {
        job?.cancel(null)
    }

    private fun resume() {
        job = Job()
        CoroutineScope(Dispatchers.Default + job!!).launch {
            do {
                onDoStick()
                delay(200)
            } while (now - startTime <= intervalMillis)
            onDoStick()
        }
    }

    private fun onDoStick() {
        val remainMillis = intervalMillis + startTime - now
        stickLiveData.postValue(remainMillis)
    }

    open fun onTicks(second: Long, text: String) = Unit

    open fun onFinished() = Unit

}