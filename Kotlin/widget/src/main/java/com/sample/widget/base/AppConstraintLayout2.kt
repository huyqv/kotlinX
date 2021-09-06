package com.sample.widget.base

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.animation.Interpolator
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.annotation.ColorInt
import androidx.annotation.StyleableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.sample.widget.R

open class AppConstraintLayout2 : ConstraintLayout {

    var path: Path? = null

    /** corner radius */
    var cornerLeftTop: Float = 0f
        set(value) {
            field = value
            postInvalidate()
        }

    var cornerRightTop: Float = 0f
        set(value) {
            field = value
            postInvalidate()
        }

    var cornerLeftBottom: Float = 0f
        set(value) {
            field = value
            postInvalidate()
        }

    var cornerRightBottom: Float = 0f
        set(value) {
            field = value
            postInvalidate()
        }

    var backgroundColor: Int? = null
        set(@ColorInt value) {
            field = value
            postInvalidate()
        }

    var strokeLineWidth: Float = 0f
        set(value) {
            field = value
            postInvalidate()
        }

    var strokeLineColor = 0XFFFFFFFF.toInt()
        set(@ColorInt value) {
            field = value
            postInvalidate()
        }

    var dashLineGap: Float = 0f
        set(value) {
            field = value
            postInvalidate()
        }

    var dashLineWidth: Float = 0f
        set(value) {
            field = value
            postInvalidate()
        }

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        attrs ?: return
        val a = context.obtainStyledAttributes(attrs, R.styleable.AppConstraintLayout)
        a.initRadius()
        a.initExpandable()
        a.recycle()
    }

    /**
     * Radius
     */
    private fun TypedArray.initRadius() {
        val radius = pixels(R.styleable.AppConstraintLayout_radius)
        if (radius != 0f) {
            cornerLeftTop = radius
            cornerRightTop = radius
            cornerLeftBottom = radius
            cornerRightBottom = radius
        } else {
            cornerLeftTop = pixels(R.styleable.AppConstraintLayout_topLeftRadius)
            cornerRightTop = pixels(R.styleable.AppConstraintLayout_topRightRadius)
            cornerLeftBottom = pixels(R.styleable.AppConstraintLayout_bottomLeftRadius)
            cornerRightBottom = pixels(R.styleable.AppConstraintLayout_bottomRightRadius)
        }

        backgroundColor = getColor(R.styleable.AppConstraintLayout_backgroundColor, Color.WHITE)
        strokeLineWidth = pixels(R.styleable.AppConstraintLayout_strokeLineWidth)
        strokeLineColor = getColor(R.styleable.AppConstraintLayout_strokeLineColor, Color.BLACK)
        dashLineWidth = pixels(R.styleable.AppConstraintLayout_dashLineWidth)
        dashLineGap = pixels(R.styleable.AppConstraintLayout_dashLineGap)
    }

    fun TypedArray.pixels(@StyleableRes id: Int): Float {
        return getDimensionPixelSize(id, 0).toFloat()
    }

    override fun dispatchDraw(canvas: Canvas) {
        /** for outline remake when ever draw */
        path = Path()

        clipPathCanvas(
            canvas, floatArrayOf(
                cornerLeftTop, cornerLeftTop, cornerRightTop, cornerRightTop, cornerRightBottom,
                cornerRightBottom, cornerLeftBottom, cornerLeftBottom
            )
        )

        /** set drawable resource corner & background & stroke */
        GradientDrawable().apply {
            cornerRadii = floatArrayOf(
                cornerLeftTop, cornerLeftTop, cornerRightTop, cornerRightTop,
                cornerRightBottom, cornerRightBottom, cornerLeftBottom, cornerLeftBottom
            )
            if (strokeLineWidth != 0f && strokeLineColor != null) {
                this.setStroke(strokeLineWidth.toInt(), strokeLineColor, dashLineWidth, dashLineGap)
            }
            backgroundColor?.let {
                setColor(it)
            } ?: setColor(Color.WHITE)

            background = this
        }

        outlineProvider = outlineProvider

        clipChildren = false

        super.dispatchDraw(canvas)
    }

    private fun clipPathCanvas(canvas: Canvas, floatArray: FloatArray) {
        path?.let {
            it.addRoundRect(
                RectF(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat()),
                floatArray,
                Path.Direction.CW
            )
            canvas.clipPath(it)
        }
    }

    override fun setBackgroundColor(color: Int) {
        backgroundColor = color
    }

    /** For not showing red underline */
    override fun setOutlineProvider(provider: ViewOutlineProvider?) {
        super.setOutlineProvider(provider)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getOutlineProvider(): ViewOutlineProvider {
        return object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                path?.let {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        outline.setPath(it)
                    } else {
                        @Suppress("DEPRECATION")
                        outline.setConvexPath(it)
                    }
                } ?: throw Exception()
            }
        }
    }

    /**
     * Radio group
     */
    var onCheckedChanged: ((RadioButton, Boolean) -> Unit)? = null

    private val checkedChangeListener: CompoundButton.OnCheckedChangeListener by lazy {
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (onCheckedChanged != null && buttonView is RadioButton) {
                onCheckedChanged!!(buttonView, isChecked)
            }
        }
    }

    override fun addViewInLayout(child: View?, index: Int, params: ViewGroup.LayoutParams?): Boolean {
        (child as? RadioButton)?.also { radioButton ->
            radioButton.setOnCheckedChangeListener(checkedChangeListener)
        }
        return super.addViewInLayout(child, index, params)
    }

    /**
     *
     */
    companion object {
        const val COLLAPSED = 0
        const val COLLAPSING = 1
        const val EXPANDING = 2
        const val EXPANDED = 3
        const val KEY_SUPER_STATE = "super_state"
        const val KEY_EXPANSION = "expansion"
        const val HORIZONTAL = 0
        const val VERTICAL = 1
        private const val DEFAULT_DURATION = 300
    }

    var duration = DEFAULT_DURATION

    var parallax: Float = 0F
        set(value) {
            var parallax = value
            parallax = 1F.coerceAtMost(0F.coerceAtLeast(parallax))
            field = value
        }

    var expansion: Float = 0F
        set(value) {
            if (this.expansion == value) {
                return
            }
            // Infer state from previous value
            val delta = value - this.expansion
            state = when {
                value == 0f -> COLLAPSED
                value == 1f -> EXPANDED
                delta < 0 -> COLLAPSING
                else -> EXPANDING
            }
            visibility = if (state == COLLAPSED) GONE else VISIBLE
            field = value
            requestLayout()
            listener?.invoke(value, state)
        }

    var orientation: Int = 0
        set(value) {
            require(!(value < 0 || value > 1)) { "Orientation must be either 0 (horizontal) or 1 (vertical)" }
            field = value
        }

    var isExpanded: Boolean
        get() = state == EXPANDING || state == EXPANDED
        set(expand) {
            setExpanded(expand, true)
        }

    lateinit var interpolator: Interpolator

    private var state = 0

    private var animator: ValueAnimator? = null

    var listener: ((/*expansionFraction*/ Float, /*state*/ Int) -> Unit)? = null

    private fun TypedArray.initExpandable() {
        interpolator = FastOutSlowInInterpolator()
        duration = getInt(R.styleable.ExpandableLayout_expandDuration, DEFAULT_DURATION)
        expansion = if (getBoolean(R.styleable.ExpandableLayout_expanded, false)) 1F else 0F
        orientation = getInt(R.styleable.ExpandableLayout_expandOrientation, VERTICAL)
        parallax = getFloat(R.styleable.ExpandableLayout_expandParallax, 1F)
        state = if (expansion == 0f) COLLAPSED else EXPANDED
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val bundle = Bundle()
        expansion = if (isExpanded) 1F else 0F
        bundle.putFloat(KEY_EXPANSION, expansion)
        bundle.putParcelable(KEY_SUPER_STATE, superState)
        return bundle
    }

    override fun onRestoreInstanceState(parcelable: Parcelable) {
        val bundle = parcelable as Bundle
        expansion = bundle.getFloat(KEY_EXPANSION)
        state = if (expansion == 1f) EXPANDED else COLLAPSED
        val superState = bundle.getParcelable<Parcelable>(KEY_SUPER_STATE)
        super.onRestoreInstanceState(superState)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = measuredHeight
        val size = if (orientation == LinearLayout.HORIZONTAL) width else height
        visibility = if (expansion == 0f && size == 0) GONE else VISIBLE
        val expansionDelta = size - Math.round(size * expansion)
        if (parallax > 0) {
            val parallaxDelta = expansionDelta * parallax
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (orientation == HORIZONTAL) {
                    var direction = -1
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && layoutDirection == LAYOUT_DIRECTION_RTL) {
                        direction = 1
                    }
                    child.translationX = direction * parallaxDelta
                } else {
                    child.translationY = -parallaxDelta
                }
            }
        }
        if (orientation == HORIZONTAL) {
            setMeasuredDimension(width - expansionDelta, height)
        } else {
            setMeasuredDimension(width, height - expansionDelta)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        animator?.cancel()
        super.onConfigurationChanged(newConfig)
    }

    @JvmOverloads
    fun toggle(animate: Boolean = true) {
        if (isExpanded) {
            collapse(animate)
        } else {
            expand(animate)
        }
    }

    @JvmOverloads
    fun expand(animate: Boolean = true) {
        setExpanded(true, animate)
    }

    @JvmOverloads
    fun collapse(animate: Boolean = true) {
        setExpanded(false, animate)
    }

    fun setExpanded(expand: Boolean, animate: Boolean) {
        if (expand == isExpanded) {
            return
        }
        val targetExpansion = if (expand) 1 else 0
        if (animate) {
            animateSize(targetExpansion)
        } else {
            expansion = targetExpansion.toFloat()
        }
    }

    private fun animateSize(targetExpansion: Int) {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(expansion, targetExpansion.toFloat()).also {
            it.interpolator = this.interpolator
            it.duration = this.duration.toLong()
            it.addUpdateListener { valueAnimator -> expansion = valueAnimator.animatedValue as Float }
            it.addListener(ExpansionListener(targetExpansion))
            it.start()
        }
    }

    abstract inner class LookupTableInterpolator(private val mValues: FloatArray) : Interpolator {
        private val mStepSize: Float = 1f / (mValues.size - 1)
        override fun getInterpolation(input: Float): Float {
            if (input >= 1.0f) {
                return 1.0f
            }
            if (input <= 0f) {
                return 0f
            }

            // Calculate index - We use min with length - 2 to avoid IndexOutOfBoundsException when
            // we lerp (linearly interpolate) in the return statement
            val position = Math.min((input * (mValues.size - 1)).toInt(), mValues.size - 2)

            // Calculate values to account for small offsets as the lookup table has discrete values
            val quantized = position * mStepSize
            val diff = input - quantized
            val weight = diff / mStepSize

            // Linearly interpolate between the table values
            return mValues[position] + weight * (mValues[position + 1] - mValues[position])
        }
    }

    private inner class ExpansionListener(private val targetExpansion: Int) :
        Animator.AnimatorListener {

        private var canceled = false

        override fun onAnimationStart(animation: Animator) {
            state = if (targetExpansion == 0) COLLAPSING else EXPANDING
        }

        override fun onAnimationEnd(animation: Animator) {
            if (!canceled) {
                state = if (targetExpansion == 0) COLLAPSED else EXPANDED
                expansion = targetExpansion.toFloat()
            }
        }

        override fun onAnimationCancel(animation: Animator) {
            canceled = true
        }

        override fun onAnimationRepeat(animation: Animator) {}
    }

    /**
     * Lookup table values sampled with x at regular intervals between 0 and 1 for a total of
     * 201 points.
     */
    inner class FastOutSlowInInterpolator : LookupTableInterpolator(floatArrayOf(
        0.0000f, 0.0001f, 0.0002f, 0.0005f, 0.0009f, 0.0014f, 0.0020f,
        0.0027f, 0.0036f, 0.0046f, 0.0058f, 0.0071f, 0.0085f, 0.0101f,
        0.0118f, 0.0137f, 0.0158f, 0.0180f, 0.0205f, 0.0231f, 0.0259f,
        0.0289f, 0.0321f, 0.0355f, 0.0391f, 0.0430f, 0.0471f, 0.0514f,
        0.0560f, 0.0608f, 0.0660f, 0.0714f, 0.0771f, 0.0830f, 0.0893f,
        0.0959f, 0.1029f, 0.1101f, 0.1177f, 0.1257f, 0.1339f, 0.1426f,
        0.1516f, 0.1610f, 0.1707f, 0.1808f, 0.1913f, 0.2021f, 0.2133f,
        0.2248f, 0.2366f, 0.2487f, 0.2611f, 0.2738f, 0.2867f, 0.2998f,
        0.3131f, 0.3265f, 0.3400f, 0.3536f, 0.3673f, 0.3810f, 0.3946f,
        0.4082f, 0.4217f, 0.4352f, 0.4485f, 0.4616f, 0.4746f, 0.4874f,
        0.5000f, 0.5124f, 0.5246f, 0.5365f, 0.5482f, 0.5597f, 0.5710f,
        0.5820f, 0.5928f, 0.6033f, 0.6136f, 0.6237f, 0.6335f, 0.6431f,
        0.6525f, 0.6616f, 0.6706f, 0.6793f, 0.6878f, 0.6961f, 0.7043f,
        0.7122f, 0.7199f, 0.7275f, 0.7349f, 0.7421f, 0.7491f, 0.7559f,
        0.7626f, 0.7692f, 0.7756f, 0.7818f, 0.7879f, 0.7938f, 0.7996f,
        0.8053f, 0.8108f, 0.8162f, 0.8215f, 0.8266f, 0.8317f, 0.8366f,
        0.8414f, 0.8461f, 0.8507f, 0.8551f, 0.8595f, 0.8638f, 0.8679f,
        0.8720f, 0.8760f, 0.8798f, 0.8836f, 0.8873f, 0.8909f, 0.8945f,
        0.8979f, 0.9013f, 0.9046f, 0.9078f, 0.9109f, 0.9139f, 0.9169f,
        0.9198f, 0.9227f, 0.9254f, 0.9281f, 0.9307f, 0.9333f, 0.9358f,
        0.9382f, 0.9406f, 0.9429f, 0.9452f, 0.9474f, 0.9495f, 0.9516f,
        0.9536f, 0.9556f, 0.9575f, 0.9594f, 0.9612f, 0.9629f, 0.9646f,
        0.9663f, 0.9679f, 0.9695f, 0.9710f, 0.9725f, 0.9739f, 0.9753f,
        0.9766f, 0.9779f, 0.9791f, 0.9803f, 0.9815f, 0.9826f, 0.9837f,
        0.9848f, 0.9858f, 0.9867f, 0.9877f, 0.9885f, 0.9894f, 0.9902f,
        0.9910f, 0.9917f, 0.9924f, 0.9931f, 0.9937f, 0.9944f, 0.9949f,
        0.9955f, 0.9960f, 0.9964f, 0.9969f, 0.9973f, 0.9977f, 0.9980f,
        0.9984f, 0.9986f, 0.9989f, 0.9991f, 0.9993f, 0.9995f, 0.9997f,
        0.9998f, 0.9999f, 0.9999f, 1.0000f, 1.0000f
    ))

}