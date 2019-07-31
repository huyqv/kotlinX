package com.huy.kotlin.base.arch

import android.content.Context
import androidx.lifecycle.ViewModel
import com.huy.kotlin.RoomHelper
import com.huy.kotlin.app.App
import com.huy.kotlin.data.local.SharedHelper
import com.huy.kotlin.data.observable.AlertLiveData
import com.huy.kotlin.data.observable.ProgressLiveData
import com.huy.kotlin.data.observable.ToastLiveData
import com.huy.kotlin.network.rest.RestClient

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/07/06
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
open class BaseViewModel : ViewModel() {

    open fun onStart() {}

    fun showProgress() {
        ProgressLiveData.show()
    }

    fun hideProgress() {
        ProgressLiveData.hide()
    }

    fun alert(string: String?) {
        AlertLiveData.message = string
    }

    fun toast(string: String?) {
        ToastLiveData.message = string
    }

    val context: Context get() = App.appContext

    val room: RoomHelper get() = RoomHelper.instance

    val shared: SharedHelper get() = SharedHelper.instance

    val service: RestClient get() = RestClient.instance

}