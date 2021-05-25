package com.kotlin.app.ui.dialog.alert

import com.kotlin.app.R

class AlertArg(

        var icon: Int = R.drawable.ic_launcher,

        var title: String? = null,

        var message: String? = null,

        // Trigger when: On touch outside dialog - on back press
        var onDismissClick: () -> Unit = {},

        var onDismiss: () -> Unit = {},

        var buttonNeutral: String? = null,

        var buttonPositive: String? = null,

        var buttonNegative: String? = null,

        var onNeutralClick: () -> Unit = {},

        var onPositiveClick: () -> Unit = {},

        var onNegativeClick: () -> Unit = {},

        )