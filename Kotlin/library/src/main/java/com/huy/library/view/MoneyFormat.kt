package com.huy.library.view

import java.text.DecimalFormat

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/03/30
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class MoneyFormat {

    private val mFormat: DecimalFormat = DecimalFormat("####,###,###")

    fun format(s: String): String? {
        return mFormat.format(s)?.replace(",".toRegex(), ".")
    }

    fun format(s: Long): String? {
        return mFormat.format(s)?.replace(",".toRegex(), ".")
    }

    companion object {

        val instance: MoneyFormat by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            MoneyFormat()
        }
    }


}
