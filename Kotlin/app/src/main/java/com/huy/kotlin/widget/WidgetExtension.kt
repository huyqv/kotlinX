package com.huy.kotlin.widget

import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2020/02/21
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */


fun SwipeRefreshLayout.onRefresh(block: () -> Unit) {
    setProgressBackgroundColorSchemeColor(ContextCompat.getColor(context, com.huy.library.R.color.colorWhite))
    setColorSchemeColors(ContextCompat.getColor(context, com.huy.library.R.color.colorPrimary))
    setOnRefreshListener(block)
}