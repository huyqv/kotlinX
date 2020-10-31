package com.kotlin.app.ui.alert

import com.kotlin.app.R

class AlertArg(
        var headerGuideline: Int = 0,
        var icon: Int = R.drawable.ic_launcher,
        var title: String? = null,
        var message: String,
        var buttonNeutral: String? = "Close",
        var buttonPositive: String? = "Close",
        var buttonNegative: String? = "Close",
        var onNeutral: (AlertFragment) -> Unit = {},
        var onPositive: (AlertFragment) -> Unit = {},
        var onNegative: (AlertFragment) -> Unit = {}
)