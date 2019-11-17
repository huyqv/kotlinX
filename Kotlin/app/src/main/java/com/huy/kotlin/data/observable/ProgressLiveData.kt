package com.huy.kotlin.data.observable

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/06/30
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ProgressLiveData : SingleLiveData<Boolean?>() {

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

        fun show(progression: Boolean) {
            if (progression) show()
        }

        fun hide(progression: Boolean) {
            if (progression) hide()
        }

    }

}