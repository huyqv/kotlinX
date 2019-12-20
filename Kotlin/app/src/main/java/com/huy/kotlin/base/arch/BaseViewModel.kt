package com.huy.kotlin.base.arch

import android.content.Context
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

    open fun onStart() {}

    val context: Context get() = App.appContext

    val room: RoomHelper get() = RoomHelper.instance

    val shared: SharedHelper get() = SharedHelper.instance

    val service: RestClient get() = RestClient.instance

}