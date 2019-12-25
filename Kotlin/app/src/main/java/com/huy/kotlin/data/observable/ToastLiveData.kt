package com.huy.kotlin.data.observable

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/06/30
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ToastLiveData private constructor() : SingleLiveData<String?>() {

    companion object {

        val instance: ToastLiveData by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ToastLiveData()
        }

        var message: String? = null
            set(string) {
                if (!string.isNullOrEmpty()) instance.postValue(string)
            }
    }

}