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
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class RoundedColorView : View {

    private lateinit var paint: Paint

    private var backgroundColor: Int = 0

    private var mSelected: Boolean = false

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ColorView)
        backgroundColor = ta.getColor(R.styleable.ColorView_backgroundColor, Color.WHITE)
        ta.recycle()
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.strokeWidth = 4f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = canvas.width
        val height = canvas.height
        paint.style = Paint.Style.FILL
        paint.color = backgroundColor
        canvas.drawCircle(width / 2f, height / 2f, width / 2f, paint)

        if (this.isSelected) {
            paint.color = Color.WHITE
            canvas.drawLine(5f * width / 16, height / 2f, 7f * width / 16, 5f * height / 8, paint)
            canvas.drawLine(7f * width / 16, 5f * height / 8, 11f * width / 16, 3f * height / 8,
                    paint)
        }
    }

    fun getBackgroundColor(): String {
        return String.format("#%06X", 0xFFFFFF and backgroundColor)
    }

    override fun setBackgroundColor(@ColorRes color: Int) {
        backgroundColor = color
        paint.color = color
        invalidate()
    }

    override fun isSelected(): Boolean {
        return mSelected
    }

    override fun setSelected(selected: Boolean) {
        mSelected = selected
        invalidate()
    }
}