package com.huy.kotlin.ui.handler

import com.huy.kotlin.ui.model.User
import com.huy.library.extension.readJsonAsset
import java.util.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: init with UserProvider.init()
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
object DataProvider {

    @Volatile
    private var isInitialized = false

    private lateinit var profiles: List<User>

    private val random = Random()

    fun init() {

        if (isInitialized) return
        isInitialized = true
        Thread(Runnable {
            val users = readJsonAsset("users.json", Array<User>::class.java)
            if (null != users) {
                profiles = users
            }
        }).start()
    }

    fun random(): User {
        return profiles[random.nextInt(profiles.size)]
    }
}