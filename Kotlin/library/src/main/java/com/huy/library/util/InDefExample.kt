package com.huy.library.util

import androidx.annotation.IntDef
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

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
    @Retention(RetentionPolicy.SOURCE)
    private annotation class Option
}