package com.huy.kotlin.ui.generator

import com.huy.kotlin.extension.parse
import com.huy.kotlin.ui.user.User
import com.huy.library.extension.readAsset
import java.util.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: init with UserProvider.init()
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
object UserProvider {

    @Volatile
    private var isInitialized = false

    private lateinit var profiles: List<User>

    private val random = Random()

    fun init() {

        if (isInitialized) return
        isInitialized = true
        Thread(Runnable {
            val json = readAsset("users.json")
            val data = json.parse(Array<User>::class.java)
            if (null != data) {
                profiles = data
            }
        }).start()
    }

    fun random(): User {
        return profiles[random.nextInt(profiles.size)]
    }
}