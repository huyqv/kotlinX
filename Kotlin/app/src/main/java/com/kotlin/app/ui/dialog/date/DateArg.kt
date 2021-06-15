package com.kotlin.app.ui.dialog.date

import java.text.SimpleDateFormat

data class DateArg(
        val key: String,
        val format: SimpleDateFormat,
        val selectedDate: String?,
        val minDate: String? = null,
        val maxDate: String? = null,
)