package com.huy.kotlin.base.event

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
interface EventListener {

    fun onEvent(id: Int, vararg args: Any?)
}