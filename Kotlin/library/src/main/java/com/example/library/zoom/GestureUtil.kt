package com.example.library.zoom

import android.view.MotionEvent
import android.widget.ImageView

object GestureUtil {

    internal fun checkZoomLevels(minZoom: Float, midZoom: Float, maxZoom: Float) {
        if (minZoom >= midZoom) {
            throw IllegalArgumentException("Minimum zoom has to be less than Medium zoom. Call setMinimumZoom() with a more appropriate value")
        } else if (midZoom >= maxZoom) {
            throw IllegalArgumentException("Medium zoom has to be less than Maximum zoom. Call setMaximumZoom() with a more appropriate value")
        }
    }

    internal fun hasDrawable(imageView: ImageView): Boolean {
        return imageView.drawable != null
    }

    internal fun isSupportedScaleType(scaleType: ImageView.ScaleType?): Boolean {
        scaleType ?: return false
        if (scaleType == ImageView.ScaleType.MATRIX)
            throw IllegalStateException("Matrix currentScale type is not supported")
        return true
    }

    internal fun getPointerIndex(action: Int): Int {
        return action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
    }
}
