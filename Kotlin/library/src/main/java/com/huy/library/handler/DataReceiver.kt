package com.huy.library.handler

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
interface DataReceiver {

    fun onDataReceived(thread: String, data: Any)

}

