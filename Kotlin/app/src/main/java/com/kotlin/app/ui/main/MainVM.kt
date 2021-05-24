package com.kotlin.app.ui.main

import androidx.lifecycle.ViewModel

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/01/18
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class MainVM : ViewModel() {

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