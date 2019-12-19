package com.huy.kotlin.ui.transaction

import com.huy.kotlin.R

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/04/13
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
object RandomColor {

    private var colors = getColors(R.color.colorTurquoise)

    fun next(): Int {
        val color = colors.random()
        colors.remove(color)
        if (colors.isEmpty()) colors = getColors(color)
        return color
    }

    private fun getColors(color: Int): MutableList<Int> {
        val colors = mutableListOf(
                R.color.colorTurquoise,
                R.color.colorPeterRiver,
                R.color.colorPink,
                R.color.colorGold,
                R.color.colorCarrot,
                R.color.colorIndianRed,
                R.color.colorGrey,
                R.color.colorPrimary
        )
        colors.remove(color)
        return colors
    }
}