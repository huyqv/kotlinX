package com.example.library.util

import androidx.annotation.IntDef

/**
 * -------------------------------------------------------------------------------------------------
 *
 * @Project: ExPrint
 * @Created: Huy 2020/09/15
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
internal object InDefExample {
    const val OPTION1 = 1
    const val OPTION2 = 2

    @IntDef(OPTION1, OPTION2)
    private annotation class Option
}