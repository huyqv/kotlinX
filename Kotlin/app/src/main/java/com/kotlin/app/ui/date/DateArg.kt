package com.kotlin.app.ui.date

import java.text.SimpleDateFormat

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/10/10
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
data class DateArg(
        val key: String,
        val format: SimpleDateFormat,
        val selectedDate: String?,
        val minDate: String? = null,
        val maxDate: String? = null,
)