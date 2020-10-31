package com.kotlin.app.app

import androidx.lifecycle.ViewModel

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/07/06
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
open class AppVM : ViewModel() {

    /**
     * View model on initialized
     */
    open fun onStart() {
    }

    /**
     * Trigger on view model initialized with network available and connectivity change to state available
     */
    open fun onNetworkAvailable() {
    }

}