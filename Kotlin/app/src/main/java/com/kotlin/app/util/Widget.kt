package com.kotlin.app.util

import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kotlin.app.R


fun SwipeRefreshLayout.onRefresh(block: () -> Unit) {
    setProgressBackgroundColorSchemeColor(ContextCompat.getColor(context, R.color.colorWhite))
    setColorSchemeColors(ContextCompat.getColor(context, R.color.colorPrimary))
    setOnRefreshListener(block)
}