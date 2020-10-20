/*
 Copyright 2011, 2012 Chris Banes.
 <p>
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 <p>
 http://www.apache.org/licenses/LICENSE-2.0
 <p>
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.example.library.zoom

import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView

/**
 * A zoomable ImageView. See [AttachedZoomView] for most of the details on how the zooming
 * is accomplished
 */
class ZoomImageView : AppCompatImageView {

    /**
     * Get the current [AttachedZoomView] for this view. Be wary of holding on to references
     * to this attachedZoomView, as it has a reference to this view, which, if a reference is held in the
     * wrong place, can cause memory leaks.
     *
     * @return the attachedZoomView.
     */
    private var attachedZoomView: AttachedZoomView? = AttachedZoomView(this)

    private var pendingScaleType: ImageView.ScaleType? = null

    var isZoomable: Boolean
        get() = attachedZoomView?.isZoomEnabled ?: false
        set(zoomable) {
            attachedZoomView?.setZoomable(zoomable)
        }

    val displayRect: RectF?
        get() = attachedZoomView?.displayRect

    var minimumScale: Float
        get() = attachedZoomView?.minimumScale ?: 1f
        set(minimumScale) {
            attachedZoomView?.minimumScale = minimumScale
        }

    var mediumScale: Float
        get() = attachedZoomView?.mediumScale ?: 1f
        set(mediumScale) {
            attachedZoomView?.mediumScale = mediumScale
        }

    var maximumScale: Float
        get() = attachedZoomView?.maximumScale ?: 1f
        set(maximumScale) {
            attachedZoomView?.maximumScale = maximumScale
        }

    var scale: Float
        get() = attachedZoomView?.currentScale ?: 1f
        set(scale) {
            attachedZoomView?.currentScale = scale
        }


    constructor(context: Context) : super(context) {
        onViewInit()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        onViewInit()
    }

    constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : super(context, attrs, defStyle) {
        onViewInit()
    }

    override fun getScaleType(): ImageView.ScaleType? {
        return attachedZoomView?.scaleType
    }

    private fun onViewInit() {
        //We always pose as a Matrix currentScale type, though we can change to another currentScale type
        //via the attachedZoomView
        super.setScaleType(ImageView.ScaleType.MATRIX)
        //apply the previously applied currentScale type
        if (pendingScaleType != null) {
            setScaleType(pendingScaleType as ScaleType)
            pendingScaleType = null
        }
    }

    override fun setScaleType(scaleType: ImageView.ScaleType) {
        if (attachedZoomView == null) {
            pendingScaleType = scaleType
        } else {
            attachedZoomView?.scaleType = scaleType
        }
    }

    override fun getImageMatrix(): Matrix? {
        return attachedZoomView?.imageMatrix
    }

    override fun setOnLongClickListener(l: View.OnLongClickListener?) {
        attachedZoomView?.setOnLongClickListener(l)
    }

    override fun setOnClickListener(l: View.OnClickListener?) {
        attachedZoomView?.setOnClickListener(l)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        // setImageBitmap calls through to this method
        attachedZoomView?.update()
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        attachedZoomView?.update()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        attachedZoomView?.update()
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        val changed = super.setFrame(l, t, r, b)
        if (changed) {
            attachedZoomView?.update()
        }
        return changed
    }

    fun setRotationTo(rotationDegree: Float) {
        attachedZoomView?.setRotationTo(rotationDegree)
    }

    fun setRotationBy(rotationDegree: Float) {
        attachedZoomView?.setRotationBy(rotationDegree)
    }

    fun getDisplayMatrix(matrix: Matrix) {
        attachedZoomView?.getDisplayMatrix(matrix)
    }

    fun setDisplayMatrix(finalRectangle: Matrix): Boolean {
        return attachedZoomView?.setDisplayMatrix(finalRectangle) ?: false
    }

    fun getSuppMatrix(matrix: Matrix) {
        attachedZoomView?.getSuppMatrix(matrix)
    }

    fun setSuppMatrix(matrix: Matrix): Boolean {
        return attachedZoomView?.setDisplayMatrix(matrix) ?: false
    }

    fun setAllowParentInterceptOnEdge(allow: Boolean) {
        attachedZoomView?.setAllowParentInterceptOnEdge(allow)
    }

    fun setScaleLevels(minimumScale: Float, mediumScale: Float, maximumScale: Float) {
        attachedZoomView?.setScaleLevels(minimumScale, mediumScale, maximumScale)
    }

    fun setOnMatrixChangeListener(listener: GestureHelper.MatrixChangedListener) {
        attachedZoomView?.setOnMatrixChangeListener(listener)
    }

    fun setOnPhotoTapListener(listener: GestureHelper.PhotoTapListener) {
        attachedZoomView?.setOnPhotoTapListener(listener)
    }

    fun setOnOutsidePhotoTapListener(listener: GestureHelper.OutsidePhotoTapListener) {
        attachedZoomView?.setOnOutsidePhotoTapListener(listener)
    }

    fun setOnViewTapListener(listener: GestureHelper.ViewTapListener) {
        attachedZoomView?.setOnViewTapListener(listener)
    }

    fun setOnViewDragListener(listener: GestureHelper.ViewDragListener) {
        attachedZoomView?.setOnViewDragListener(listener)
    }

    fun setScale(scale: Float, animate: Boolean) {
        attachedZoomView?.setScale(scale, animate)
    }

    fun setScale(scale: Float, focalX: Float, focalY: Float, animate: Boolean) {
        attachedZoomView?.setScale(scale, focalX, focalY, animate)
    }

    fun setZoomTransitionDuration(milliseconds: Int) {
        attachedZoomView?.setZoomTransitionDuration(milliseconds)
    }

    fun setOnDoubleTapListener(onDoubleTapListener: GestureDetector.OnDoubleTapListener) {
        attachedZoomView?.setOnDoubleTapListener(onDoubleTapListener)
    }

    fun setOnScaleChangeListener(scaleChangedListener: GestureHelper.ScaleChangedListener) {
        attachedZoomView?.setOnScaleChangeListener(scaleChangedListener)
    }

    fun setOnSingleFlingListener(singleFlingListener: GestureHelper.SingleFlingListener) {
        attachedZoomView?.setOnSingleFlingListener(singleFlingListener)
    }
}
