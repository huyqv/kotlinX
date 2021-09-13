package com.kotlin.app.shared

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kotlin.app.R

fun SwipeRefreshLayout.onRefresh(block: () -> Unit) {
    setProgressBackgroundColorSchemeColor(
        androidx.core.content.ContextCompat.getColor(
            context,
            R.color.colorWhite
        )
    )
    setColorSchemeColors(
        androidx.core.content.ContextCompat.getColor(
            context,
            R.color.colorPrimary
        )
    )
    setOnRefreshListener(block)
}