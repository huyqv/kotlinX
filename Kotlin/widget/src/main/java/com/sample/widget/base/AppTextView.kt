package com.sample.widget.base

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.sample.widget.R

class AppTextView : AppCompatTextView {

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs)

    private var errorText: String? = null

    val hasError get() = !errorText.isNullOrEmpty()

    override fun setError(error: CharSequence?) {
        super.setText(error, null)
        errorText = error?.toString()
        validateErrorColor()
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        errorText = null
        validateErrorColor()
    }

    private fun validateErrorColor() {
        this.post {
            if (errorText.isNullOrEmpty()) {
                clearColor()
            } else {
                setTextColorRes(R.color.colorFusionRed, R.color.colorDesire)
            }
        }
        invalidate()
    }

    fun setTextColorRes(@ColorRes topColor: Int, @ColorRes bottomColor: Int) {
        setTextColor(
            ContextCompat.getColor(context, topColor),
            ContextCompat.getColor(context, bottomColor)
        )
    }

    fun setTextColor(@ColorInt topColor: Int, @ColorInt bottomColor: Int) {
        val shader = LinearGradient(
            0f, 0f, 0f, lineHeight.toFloat(),
            topColor,
            bottomColor,
            Shader.TileMode.CLAMP
        )
        paint.shader = shader
    }

    private fun clearColor() {
        paint.shader = null
    }

}