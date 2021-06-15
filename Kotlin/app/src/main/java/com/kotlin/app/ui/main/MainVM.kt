package com.kotlin.app.ui.main

import androidx.lifecycle.ViewModel

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