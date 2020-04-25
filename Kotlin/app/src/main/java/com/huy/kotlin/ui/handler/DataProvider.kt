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

    private lateinit var profiles: List<User>

    private val mRandom = Random()

    val random: User get() = profiles[mRandom.nextInt(profiles.size)]

    fun init() {
        profiles = readJsonAsset("user_list.json", Array<User>::class.java)!!
    }
}