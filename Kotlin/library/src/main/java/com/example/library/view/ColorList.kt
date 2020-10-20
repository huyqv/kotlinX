package com.example.library.view

import android.graphics.Color
import java.util.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/07/08
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ColorList {

    enum class Mode {
        RANDOM, ORDER
    }

    private val randomGenerator: Random = Random()
    private val mode: Mode
    private var colorList: MutableList<Int>

    constructor(mode: Mode = Mode.ORDER) {
        this.mode = mode
        colorList = mutableListOf()
    }

    fun nextColor(): Int {

        if (colorList.isEmpty()) initColorList()

        return if (mode == Mode.RANDOM) {

            val index = randomGenerator.nextInt(colorList.size)
            val color = colorList[index]
            colorList.removeAt(index)
            color

        } else {

            val color = colorList[0]
            colorList.removeAt(0)
            color
        }
    }

    private fun initColorList() {
        colorList.addAll(listOf(
                Color.rgb(133, 68, 214),
                Color.rgb(40, 168, 201),
                Color.rgb(192, 61, 51),
                Color.rgb(79, 173, 45),
                Color.rgb(253, 201, 43),
                Color.rgb(205, 65, 164),
                Color.rgb(22, 138, 205),
                Color.rgb(206, 103, 27),
                Color.rgb(141, 89, 13)
        ))
    }

}