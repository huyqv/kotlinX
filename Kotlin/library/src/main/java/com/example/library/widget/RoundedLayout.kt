package com.example.library.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.library.R

/**
 * -------------------------------------------------------------------------------------------------
 *
 * @Project: Kotlin
 * @Created: Huy 2020/10/12
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class RoundedLayout : ConstraintLayout {

    private var radius: Float = 0f
    private var topLeftRadius: Float = 0f
    private var topRightRadius: Float = 0f
    private var bottomLeftRadius: Float = 0f
    private var bottomRightRadius: Float = 0f

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        onViewInit(context, attrs)
    }

    private fun onViewInit(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedLayout, 0, 0)
        radius = typedArray.getDimension(R.styleable.RoundedLayout_radius, 0f)
        topLeftRadius = typedArray.getDimension(R.styleable.RoundedLayout_topLeftRadius, radius)
        topRightRadius = typedArray.getDimension(R.styleable.RoundedLayout_topRightRadius, radius)
        bottomLeftRadius = typedArray.getDimension(R.styleable.RoundedLayout_bottomLeftRadius, radius)
        bottomRightRadius = typedArray.getDimension(R.styleable.RoundedLayout_bottomRightRadius, radius)
        typedArray.recycle()
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun dispatchDraw(canvas: Canvas) {
        val count = canvas.save()
        val path = Path()
        val cornerDimensions = floatArrayOf(
                topLeftRadius, topLeftRadius,
                topRightRadius, topRightRadius,
                bottomRightRadius, bottomRightRadius,
                bottomLeftRadius, bottomLeftRadius)
        path.addRoundRect(RectF(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat()), cornerDimensions, Path.Direction.CW)
        canvas.clipPath(path)
        super.dispatchDraw(canvas)
        canvas.restoreToCount(count)
    }

}