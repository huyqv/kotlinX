package com.huy.kotlin.ui.handle_thread

import com.huy.kotlin.ui.user.User
import com.huy.library.handler.DataReceiver
import com.huy.library.handler.RepeatHandlerThread

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class UserGenerator : RepeatHandlerThread<User> {

    constructor(receiver: DataReceiver<User>) : super("UserGenerator", 400, receiver)

    override fun onDataGenerate(): User {
        return UserProvider.random()
    }

}