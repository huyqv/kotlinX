package com.kotlin.app.ui.alert

import com.kotlin.app.R

class AlertArg(

        var icon: Int = R.drawable.ic_launcher,

        var title: String? = null,

        var message: String,

        // Trigger when: On touch outside dialog - on back press
        var onDismissClick: () -> Unit = {},

        var onDismiss: () -> Unit = {},

        var buttonNeutral: String? = "Close",

        var buttonPositive: String? = "Close",

        var buttonNegative: String? = "Close",

        var onNeutralClick: () -> Unit = {},

        var onPositiveClick: () -> Unit = {},

        var onNegativeClick: () -> Unit = {},
)