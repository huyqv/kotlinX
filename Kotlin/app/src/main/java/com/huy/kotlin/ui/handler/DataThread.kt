package com.huy.kotlin.ui.handler

import com.huy.kotlin.ui.model.User
import com.huy.library.thread.RepeatThread

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class DataThread : RepeatThread("UserGenerator", 400) {

    override fun onDataGenerate(): User? {
        return DataProvider.random()
    }

}