package com.huy.library.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.LinearLayoutCompat
import com.huy.library.R

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/10/14
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class RoundedLayout : LinearLayoutCompat {

    private var maskBitmap: Bitmap? = null

    private var paint: Paint? = null

    private var maskPaint: Paint? = null

    private var cornerRadius: Float = 0.toFloat()

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.RoundedLayout, 0, 0)
        try {
            val metrics = context.resources.displayMetrics
            val dp = a.getLayoutDimension(R.styleable.RoundedLayout_android_radius, 0)
            cornerRadius = dp * metrics.density
            paint = Paint(Paint.ANTI_ALIAS_FLAG)
            maskPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
            maskPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            setWillNotDraw(false)
        } finally {
            a.recycle()
        }
    }

    override fun draw(canvas: Canvas) {

        val offscreenBitmap = Bitmap.createBitmap(canvas.width, canvas.height, Bitmap.Config.ARGB_8888)
        val offscreenCanvas = Canvas(offscreenBitmap)
        super.draw(offscreenCanvas)
        if (maskBitmap == null) {
            maskBitmap = createMask(canvas.width, canvas.height)
        }
        offscreenCanvas.drawBitmap(maskBitmap!!, 0f, 0f, maskPaint)
        canvas.drawBitmap(offscreenBitmap, 0f, 0f, paint)
    }

    private fun createMask(width: Int, height: Int): Bitmap {
        val mask = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8)
        val canvas = Canvas(mask)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        canvas.drawRoundRect(RectF(0f, 0f, width.toFloat(), height.toFloat()), cornerRadius, cornerRadius, paint)
        return mask
    }
}