package com.huy.kotlin.data.observable

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/06/30
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class AlertLiveData : SingleLiveData<String?>() {

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