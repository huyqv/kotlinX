package com.example.kotlin.data.observable

import com.example.kotlin.base.ext.EventLiveData

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/06/30
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ProgressLiveData private constructor() : EventLiveData<Boolean>() {

    @Volatile
    var animated = false

    companion object {

        val instance: ProgressLiveData by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ProgressLiveData()
        }

        fun show() {
            if (!instance.animated) {
                instance.postValue(true)
                instance.animated = true
            }
        }

        fun hide() {
            if (instance.animated) {
                instance.postValue(false)
                instance.animated = false
            }
        }


    }

}