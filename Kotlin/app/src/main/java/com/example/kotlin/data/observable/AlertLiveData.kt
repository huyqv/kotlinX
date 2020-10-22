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
class AlertLiveData private constructor() : EventLiveData<String>() {

    companion object {

        val instance: AlertLiveData by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            AlertLiveData()
        }

        var message: String? = null
            set(string) {
                if (!string.isNullOrEmpty()) instance.postValue(string)
            }
    }


}