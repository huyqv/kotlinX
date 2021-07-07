package com.sample.widget.base

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.sample.widget.R
import kotlin.math.floor

class DashView : View {

    private var dashHeight: Float = 1f

    private var dashLength: Float = 10f

    private var dashSpace: Float = 10f

    var orientation: Int = HORIZONTAL

    val path: Path = Path()

    private val paint: Paint = Paint()

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        val types = context.theme.obtainStyledAttributes(attrs, R.styleable.DashView, 0, 0)
        dashLength = types.getDimension(R.styleable.DashView_dashLength, dashLength)
        dashSpace = types.getDimension(R.styleable.DashView_dashSpace, dashSpace)
        paint.color = types.getColor(R.styleable.DashView_dashColor, Color.BLACK)
        types.recycle()
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