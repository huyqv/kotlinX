package com.huy.kotlin.data.observable

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/04/19
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class NetworkLiveData : SingleLiveData<Boolean?>() {

    companion object {

        val instance: NetworkLiveData by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkLiveData()
        }

        var isConnected: Boolean?
            get() = instance.value
            set(value) {
                instance.value = value
            }
    }
}

