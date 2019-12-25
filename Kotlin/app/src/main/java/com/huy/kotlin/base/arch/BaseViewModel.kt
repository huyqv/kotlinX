package com.huy.kotlin.base.arch

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.huy.kotlin.RoomHelper
import com.huy.kotlin.app.App
import com.huy.kotlin.data.local.SharedHelper
import com.huy.kotlin.network.rest.RestClient

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/07/06
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
open class BaseViewModel : ViewModel() {

    val context: Context get() = App.appContext

    val room: RoomHelper get() = RoomHelper.instance

    val shared: SharedHelper get() = SharedHelper.instance

    val service: RestClient get() = RestClient.instance


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