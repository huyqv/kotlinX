package com.huy.library.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import com.huy.library.R

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/28
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class SquareColorView : View {

    private lateinit var paint: Paint
    private var backgroundColor: Int = 0

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {

        val ta = context.obtainStyledAttributes(attrs, R.styleable.ColorView)
        backgroundColor = ta.getColor(R.styleable.ColorView_backgroundColor, Color.WHITE)
        ta.recycle()
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)
        val width = canvas.width.toFloat()
        paint.style = Paint.Style.FILL
        paint.color = backgroundColor
        canvas.drawRect(0f, 0f, width, width, paint)
    }

    fun getBackgroundColor(): String {

        return String.format("#%06X", 0xFFFFFF and backgroundColor)
    }

    override fun setBackgroundColor(@ColorRes color: Int) {

        backgroundColor = color
        paint.color = color
        invalidate()
    }

}