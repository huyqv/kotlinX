package com.huy.library.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.huy.library.R
import kotlin.math.floor

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/12/10
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class DashView constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    var dashHeight: Float = 1f

    var dashLength: Float = 10f

    var dashSpace: Float = 10f

    var orientation: Int = HORIZONTAL

    val path: Path = Path()

    val paint: Paint = Paint()

    init {
        if (attrs != null) context?.theme?.obtainStyledAttributes(attrs, R.styleable.DashView, defStyleAttr, 0)?.apply {
            dashLength = getDimension(R.styleable.DashView_dashView_dashLength, dashLength)
            dashSpace = getDimension(R.styleable.DashView_dashView_dashSpace, dashSpace)
            paint.color = getColor(R.styleable.DashView_dashView_color, Color.argb(255, 0, 0, 0))
            recycle()
        }
        paint.strokeWidth = dashHeight
        paint.style = Paint.Style.STROKE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (orientation == HORIZONTAL) {
            val widthNeeded = paddingLeft + paddingRight + suggestedMinimumWidth
            val width = resolveSize(widthNeeded, widthMeasureSpec)
            val heightNeeded = paddingTop + paddingBottom + dashHeight
            val height = resolveSize(heightNeeded.toInt(), heightMeasureSpec)
            setMeasuredDimension(width, height)
        } else {
            val widthNeeded = paddingLeft + paddingRight + dashHeight
            val width = resolveSize(widthNeeded.toInt(), widthMeasureSpec)
            val heightNeeded = paddingTop + paddingBottom + suggestedMinimumHeight
            val height = resolveSize(heightNeeded, heightMeasureSpec)
            setMeasuredDimension(width, height)
        }
    }

    override fun onDraw(canvas: Canvas) {
        path.reset()
        path.moveTo(paddingLeft.toFloat(), paddingTop.toFloat())
        if (orientation == HORIZONTAL) {
            val w = canvas.width - paddingLeft - paddingRight
            val d = dashLength
            val m = dashSpace
            val c: Int = floor(((w - d) / (d + m)).toDouble()).toInt()
            val g: Float = (w - (d * (c + 1))) / c
            path.lineTo((canvas.width - paddingLeft - paddingRight).toFloat(), paddingTop.toFloat())
            paint.pathEffect = DashPathEffect(floatArrayOf(d, g), 0f)
        } else {
            val h = (canvas.height - paddingTop - paddingBottom).toFloat()
            val d = dashLength
            val m = dashSpace
            val c: Int = floor(((h - d) / (d + m)).toDouble()).toInt()
            val g: Float = (h - (d * (c + 1))) / c
            paint.pathEffect = DashPathEffect(floatArrayOf(d, g), 0f)
            path.lineTo(paddingLeft.toFloat(), h)
        }
        canvas.drawPath(path, paint)
    }

    companion object {
        const val HORIZONTAL: Int = 0
        const val VERTICAL: Int = 1
    }

}