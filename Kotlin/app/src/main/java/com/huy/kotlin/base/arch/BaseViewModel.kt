package com.huy.kotlin.base.arch

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.huy.kotlin.App
import com.huy.kotlin.data.RoomDB
import com.huy.kotlin.data.Shared
import com.huy.kotlin.data.api.ApiClient

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/07/06
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
open class BaseViewModel : ViewModel() {

    val context: Context get() = App.instance.applicationContext

    val room: RoomDB get() = RoomDB.instance

    val shared: Shared get() = Shared.instance

    val service: ApiClient get() = ApiClient.instance

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

    /**
     * Bundle for passing data from view
     */
    val bundle: Bundle by lazy { Bundle() }

    fun bundle(block: Bundle.() -> Unit) {
        bundle.block()
    }

    fun clearBunble() {
        bundle.clear()
    }

}