package com.sample.library.adapter

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RtlGridLayoutManager : GridLayoutManager {

    constructor(
        context: Context?,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    constructor(
        context: Context?,
        spanCount: Int = 1,
        orientation: Int = RecyclerView.VERTICAL,
        reverseLayout: Boolean = false
    ) : super(context, spanCount, orientation, reverseLayout)

    override fun isLayoutRTL(): Boolean {
        return true
    }
}