package com.sample.library.zoom

import android.content.Context
import android.graphics.Matrix
import android.graphics.Matrix.ScaleToFit
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import android.view.View.OnLongClickListener
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.OverScroller
import androidx.appcompat.widget.AppCompatImageView

/**
 * The component of [ZoomImageView] which does the work allowing for zooming, scaling, panning, etc.
 * It is made public in case you need to subclass something other than AppCompatImageView and still
 * gain the functionality that [ZoomImageView] offers
 */
class AttachedZoomView : View.OnTouchListener, View.OnLayoutChangeListener {

    companion object {
        private const val EDGE_NONE = -1
        private const val EDGE_LEFT = 0
        private const val EDGE_RIGHT = 1
        private const val EDGE_BOTH = 2
        private const val DEFAULT_MAX_SCALE = 3.0f
        private const val DEFAULT_MID_SCALE = 1.75f
        private const val DEFAULT_MIN_SCALE = 1.0f
        private const val DEFAULT_ZOOM_DURATION = 200
        private const val SINGLE_TOUCH = 1
    }

    // These are set so we don't keep allocating them on the heap
    private val baseMatrix = Matrix()

    val imageMatrix = Matrix()

    private val suppMatrix = Matrix()

    private val mDisplayRect = RectF()

    private val matrixValues = FloatArray(9)

    private var interpolator: Interpolator = AccelerateDecelerateInterpolator()

    private var zoomDuration = DEFAULT_ZOOM_DURATION

    private var minScale = DEFAULT_MIN_SCALE

    private var midScale = DEFAULT_MID_SCALE

    private var maxScale = DEFAULT_MAX_SCALE

    private var allowParentInterceptOnEdge = true

    private var blockParentIntercept = false

    // Gesture Detectors
    private var gestureDetector: android.view.GestureDetector? = null

    private var scaleDragDetector: GestureDetector? = null

    // Listeners
    private var onClickListener: View.OnClickListener? = null

    private var longClickListener: OnLongClickListener? = null

    private var matrixChangeListener: GestureHelper.MatrixChangedListener? = null

    private var photoTapListener: GestureHelper.PhotoTapListener? = null

    private var outsidePhotoTapListener: GestureHelper.OutsidePhotoTapListener? = null

    private var viewTapListener: GestureHelper.ViewTapListener? = null

    private var scaleChangeListener: GestureHelper.ScaleChangedListener? = null

    private var singleFlingListener: GestureHelper.SingleFlingListener? = null

    private var viewDragListener: GestureHelper.ViewDragListener? = null

    private var currentFlingRunnable: FlingRunnable? = null

    private var scrollEdge = EDGE_BOTH

    private var baseRotation: Float = 0.toFloat()

    var isZoomEnabled: Boolean = true
        private set

    var scaleType = ScaleType.FIT_CENTER
        set(scaleType) {
            if (GestureUtil.isSupportedScaleType(scaleType) && scaleType != this.scaleType) {
                field = scaleType
                update()
            }
        }

    private val gestureListener = object : GestureHelper.GestureListener {
        override fun onDrag(dx: Float, dy: Float) {
            if (scaleDragDetector?.isScaling == true) {
                return  // Do not drag if we are already scaling
            }
            viewDragListener?.onDrag(dx, dy)
            suppMatrix.postTranslate(dx, dy)
            checkAndDisplayMatrix()

            /*
             * Here we decide whether to let the ImageView's parent to start taking
             * over the touch event.
             *
             * First we check whether this function is enabled. We never want the
             * parent to take over if we're scaling. We then check the edge we're
             * on, and the direction of the scroll (i.e. if we're pulling against
             * the edge, aka 'overscrolling', let the parent take over).
             */
            val parent = imageView.parent
            if (allowParentInterceptOnEdge && scaleDragDetector?.isScaling != true && !blockParentIntercept) {
                if (scrollEdge == EDGE_BOTH || scrollEdge == EDGE_LEFT && dx >= 1f || scrollEdge == EDGE_RIGHT && dx <= -1f) {
                    parent?.requestDisallowInterceptTouchEvent(false)
                }
            } else {
                parent?.requestDisallowInterceptTouchEvent(true)
            }
        }

        override fun onFling(startX: Float, startY: Float, velocityX: Float, velocityY: Float) {
            currentFlingRunnable = FlingRunnable(imageView.context)
            currentFlingRunnable!!.fling(
                getImageViewWidth(imageView),
                getImageViewHeight(imageView), velocityX.toInt(), velocityY.toInt()
            )
            imageView.post(currentFlingRunnable)
        }

        override fun onScale(scaleFactor: Float, focusX: Float, focusY: Float) {
            if ((currentScale < maxScale || scaleFactor < 1f) && (currentScale > minScale || scaleFactor > 1f)) {
                if (scaleChangeListener != null) {
                    scaleChangeListener!!.onScaleChange(scaleFactor, focusX, focusY)
                }
                suppMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY)
                checkAndDisplayMatrix()
            }
        }
    }

    val displayRect: RectF?
        get() {
            checkMatrixBounds()
            return getDisplayRect(drawMatrix)
        }

    var minimumScale: Float
        get() = minScale
        set(minimumScale) {
            GestureUtil.checkZoomLevels(minimumScale, midScale, maxScale)
            minScale = minimumScale
        }

    var mediumScale: Float
        get() = midScale
        set(mediumScale) {
            GestureUtil.checkZoomLevels(minScale, mediumScale, maxScale)
            midScale = mediumScale
        }

    var maximumScale: Float
        get() = maxScale
        set(maximumScale) {
            GestureUtil.checkZoomLevels(minScale, midScale, maximumScale)
            maxScale = maximumScale
        }

    var currentScale: Float
        get() = Math.sqrt(
            (Math.pow(getValue(suppMatrix, Matrix.MSCALE_X).toDouble(), 2.0).toFloat() + Math.pow(
                getValue(suppMatrix, Matrix.MSKEW_Y).toDouble(),
                2.0
            ).toFloat()).toDouble()
        ).toFloat()
        set(scale) = setScale(scale, false)

    private val drawMatrix: Matrix
        get() {
            imageMatrix.set(baseMatrix)
            imageMatrix.postConcat(suppMatrix)
            return imageMatrix
        }

    private val imageView: AppCompatImageView

    constructor(view: AppCompatImageView) {
        imageView = view
        imageView.setOnTouchListener(this)
        imageView.addOnLayoutChangeListener(this)
        if (imageView.isInEditMode) return

        baseRotation = 0.0f
        // Create Gesture Detectors...
        scaleDragDetector = GestureDetector(imageView.context, gestureListener)
        gestureDetector = android.view.GestureDetector(
            imageView.context,
            object : android.view.GestureDetector.SimpleOnGestureListener() {

                // forward long click listener
                override fun onLongPress(e: MotionEvent) {
                    if (longClickListener != null) {
                        longClickListener!!.onLongClick(imageView)
                    }
                }

                override fun onFling(
                    e1: MotionEvent, e2: MotionEvent,
                    velocityX: Float, velocityY: Float,
                ): Boolean {
                    if (singleFlingListener != null) {
                        if (currentScale > DEFAULT_MIN_SCALE) {
                            return false
                        }
                        return if (e1.pointerCount > SINGLE_TOUCH || e2.pointerCount > SINGLE_TOUCH) {
                            false
                        } else singleFlingListener!!.onSingleFling(e1, e2, velocityX, velocityY)
                    }
                    return false
                }
            })
        gestureDetector?.setOnDoubleTapListener(object :
            android.view.GestureDetector.OnDoubleTapListener {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                onClickListener?.onClick(imageView)
                val rect = displayRect
                val x = e.x
                val y = e.y
                viewTapListener?.onViewTap(imageView, x, y)
                rect ?: return false
                // Check to see if the user tapped on the photo
                return if (rect.contains(x, y)) {
                    val xResult = (x - rect.left) / rect.width()
                    val yResult = (y - rect.top) / rect.height()
                    photoTapListener?.onPhotoTap(imageView, xResult, yResult)
                    true
                } else {
                    outsidePhotoTapListener?.onOutsidePhotoTap(imageView)
                    false
                }
            }

            override fun onDoubleTap(ev: MotionEvent): Boolean {
                try {
                    val x = ev.x
                    val y = ev.y
                    val scale = currentScale
                    if (scale < mediumScale) {
                        setScale(mediumScale, x, y, true)
                    } else if (scale >= mediumScale && scale < maximumScale) {
                        setScale(maximumScale, x, y, true)
                    } else {
                        setScale(minimumScale, x, y, true)
                    }
                } catch (e: ArrayIndexOutOfBoundsException) {
                    // Can sometimes happen when getX() and getY() is called
                }
                return true
            }

            override fun onDoubleTapEvent(e: MotionEvent): Boolean {
                // Wait for the confirmed onDoubleTap() instead
                return false
            }
        })
    }

    fun setOnDoubleTapListener(newOnDoubleTapListener: android.view.GestureDetector.OnDoubleTapListener) {
        this.gestureDetector!!.setOnDoubleTapListener(newOnDoubleTapListener)
    }

    fun setOnScaleChangeListener(listener: GestureHelper.ScaleChangedListener) {
        scaleChangeListener = listener
    }

    fun setOnSingleFlingListener(listener: GestureHelper.SingleFlingListener) {
        singleFlingListener = listener
    }

    fun setDisplayMatrix(finalMatrix: Matrix?): Boolean {
        if (finalMatrix == null) {
            throw IllegalArgumentException("Matrix cannot be null")
        }
        if (imageView.drawable == null) {
            return false
        }
        suppMatrix.set(finalMatrix)
        checkAndDisplayMatrix()
        return true
    }

    fun setBaseRotation(degrees: Float) {
        baseRotation = degrees % 360
        update()
        setRotationBy(baseRotation)
        checkAndDisplayMatrix()
    }

    fun setRotationTo(degrees: Float) {
        suppMatrix.setRotate(degrees % 360)
        checkAndDisplayMatrix()
    }

    fun setRotationBy(degrees: Float) {
        suppMatrix.postRotate(degrees % 360)
        checkAndDisplayMatrix()
    }

    override fun onLayoutChange(
        v: View,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        oldLeft: Int,
        oldTop: Int,
        oldRight: Int,
        oldBottom: Int
    ) {
        // Update our base matrix, as the bounds have changed
        if (left != oldLeft || top != oldTop || right != oldRight || bottom != oldBottom) {
            updateBaseMatrix(imageView.drawable)
        }
    }

    override fun onTouch(v: View, ev: MotionEvent): Boolean {
        var handled = false
        if (isZoomEnabled && GestureUtil.hasDrawable(v as ImageView)) {
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    val parent = v.getParent()
                    // First, disable the Parent from intercepting the touch
                    // event
                    parent?.requestDisallowInterceptTouchEvent(true)
                    // If we're flinging, and the user presses down, cancel
                    // fling
                    cancelFling()
                }
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP ->
                    // If the user has zoomed less than min scale, zoom back
                    // to min scale
                    if (currentScale < minScale) {
                        val rect = displayRect
                        if (rect != null) {
                            v.post(
                                AnimatedZoomRunnable(
                                    currentScale, minScale,
                                    rect.centerX(), rect.centerY()
                                )
                            )
                            handled = true
                        }
                    } else if (currentScale > maxScale) {
                        val rect = displayRect
                        if (rect != null) {
                            v.post(
                                AnimatedZoomRunnable(
                                    currentScale, maxScale,
                                    rect.centerX(), rect.centerY()
                                )
                            )
                            handled = true
                        }
                    }
            }
            // Try the Scale/Drag detector
            if (scaleDragDetector != null) {
                val wasScaling = scaleDragDetector?.isScaling ?: true
                val wasDragging = scaleDragDetector?.isDragging ?: true
                handled = scaleDragDetector?.onTouchEvent(ev) ?: true
                val notScaling = !wasScaling && !(scaleDragDetector?.isScaling ?: false)
                val notDragging = !wasDragging && !(scaleDragDetector?.isDragging ?: false)
                blockParentIntercept = notScaling && notDragging
            }
            // Check to see if the user double tapped
            if (gestureDetector != null && gestureDetector?.onTouchEvent(ev) == true) {
                handled = true
            }
        }
        return handled
    }

    fun setAllowParentInterceptOnEdge(allow: Boolean) {
        allowParentInterceptOnEdge = allow
    }

    fun setScaleLevels(minimumScale: Float, mediumScale: Float, maximumScale: Float) {
        GestureUtil.checkZoomLevels(minimumScale, mediumScale, maximumScale)
        minScale = minimumScale
        midScale = mediumScale
        maxScale = maximumScale
    }

    fun setOnLongClickListener(listener: OnLongClickListener?) {
        longClickListener = listener
    }

    fun setOnClickListener(listener: View.OnClickListener?) {
        onClickListener = listener
    }

    fun setOnMatrixChangeListener(listener: GestureHelper.MatrixChangedListener) {
        matrixChangeListener = listener
    }

    fun setOnPhotoTapListener(listener: GestureHelper.PhotoTapListener) {
        photoTapListener = listener
    }

    fun setOnOutsidePhotoTapListener(listener: GestureHelper.OutsidePhotoTapListener) {
        outsidePhotoTapListener = listener
    }

    fun setOnViewTapListener(listener: GestureHelper.ViewTapListener) {
        viewTapListener = listener
    }

    fun setOnViewDragListener(listener: GestureHelper.ViewDragListener) {
        viewDragListener = listener
    }

    fun setScale(scale: Float, animate: Boolean) {
        setScale(scale, (imageView.right / 2).toFloat(), (imageView.bottom / 2).toFloat(), animate)
    }

    fun setScale(scale: Float, focalX: Float, focalY: Float, animate: Boolean) {
        // Check to see if the scale is within bounds
        if (scale < minScale || scale > maxScale)
            throw IllegalArgumentException("Scale must be within the range of minScale and maxScale")

        if (animate) {
            imageView.post(AnimatedZoomRunnable(scale, scale, focalX, focalY))
        } else {
            suppMatrix.setScale(scale, scale, focalX, focalY)
            checkAndDisplayMatrix()
        }
    }

    /**
     * Set the zoom interpolator
     *
     * @param interpolator the zoom interpolator
     */
    fun setZoomInterpolator(interpolator: Interpolator) {
        this.interpolator = interpolator
    }

    fun setZoomable(zoomable: Boolean) {
        isZoomEnabled = zoomable
        update()
    }

    fun update() {
        if (isZoomEnabled) {
            // Update the base matrix using the current drawable
            updateBaseMatrix(imageView.drawable)
        } else {
            // Reset the Matrix...
            resetMatrix()
        }
    }

    /**
     * Get the display matrix
     *
     * @param matrix target matrix to copy to
     */
    fun getDisplayMatrix(matrix: Matrix) {
        matrix.set(drawMatrix)
    }

    /**
     * Get the current support matrix
     */
    fun getSuppMatrix(matrix: Matrix) {
        matrix.set(suppMatrix)
    }

    fun setZoomTransitionDuration(milliseconds: Int) {
        this.zoomDuration = milliseconds
    }

    /**
     * Helper method that 'unpacks' a Matrix and returns the required value
     *
     * @param matrix     Matrix to unpack
     * @param whichValue Which value from Matrix.M* to return
     * @return returned value
     */
    private fun getValue(matrix: Matrix, whichValue: Int): Float {
        matrix.getValues(matrixValues)
        return matrixValues[whichValue]
    }

    /**
     * Resets the Matrix back to FIT_CENTER, and then displays its contents
     */
    private fun resetMatrix() {
        suppMatrix.reset()
        setRotationBy(baseRotation)
        setImageViewMatrix(drawMatrix)
        checkMatrixBounds()
    }

    private fun setImageViewMatrix(matrix: Matrix) {
        imageView.imageMatrix = matrix
        // Call MatrixChangedListener if needed
        if (matrixChangeListener != null) {
            val displayRect = getDisplayRect(matrix)
            if (displayRect != null) {
                matrixChangeListener!!.onMatrixChanged(displayRect)
            }
        }
    }

    /**
     * Helper method that simply checks the Matrix, and then displays the result
     */
    private fun checkAndDisplayMatrix() {
        if (checkMatrixBounds()) {
            setImageViewMatrix(drawMatrix)
        }
    }

    /**
     * Helper method that maps the supplied Matrix to the current Drawable
     *
     * @param matrix - Matrix to map Drawable against
     * @return RectF - Displayed Rectangle
     */
    private fun getDisplayRect(matrix: Matrix): RectF? {
        val d = imageView.drawable
        if (d != null) {
            mDisplayRect.set(
                0f, 0f, d.intrinsicWidth.toFloat(),
                d.intrinsicHeight.toFloat()
            )
            matrix.mapRect(mDisplayRect)
            return mDisplayRect
        }
        return null
    }

    /**
     * Calculate Matrix for FIT_CENTER
     *
     * @param drawable - Drawable being displayed
     */
    private fun updateBaseMatrix(drawable: Drawable?) {
        if (drawable == null) {
            return
        }
        val viewWidth = getImageViewWidth(imageView).toFloat()
        val viewHeight = getImageViewHeight(imageView).toFloat()
        val drawableWidth = drawable.intrinsicWidth
        val drawableHeight = drawable.intrinsicHeight
        baseMatrix.reset()
        val widthScale = viewWidth / drawableWidth
        val heightScale = viewHeight / drawableHeight
        when (scaleType) {
            ScaleType.CENTER -> baseMatrix.postTranslate(
                (viewWidth - drawableWidth) / 2f,
                (viewHeight - drawableHeight) / 2f
            )
            ScaleType.CENTER_CROP -> {
                val scale = Math.max(widthScale, heightScale)
                baseMatrix.postScale(scale, scale)
                baseMatrix.postTranslate(
                    (viewWidth - drawableWidth * scale) / 2f,
                    (viewHeight - drawableHeight * scale) / 2f
                )
            }
            ScaleType.CENTER_INSIDE -> {
                val scale = Math.min(1.0f, Math.min(widthScale, heightScale))
                baseMatrix.postScale(scale, scale)
                baseMatrix.postTranslate(
                    (viewWidth - drawableWidth * scale) / 2f,
                    (viewHeight - drawableHeight * scale) / 2f
                )
            }
            else -> {
                var mTempSrc = RectF(0f, 0f, drawableWidth.toFloat(), drawableHeight.toFloat())
                val mTempDst = RectF(0f, 0f, viewWidth, viewHeight)
                if (baseRotation.toInt() % 180 != 0) {
                    mTempSrc = RectF(0f, 0f, drawableHeight.toFloat(), drawableWidth.toFloat())
                }
                when (scaleType) {
                    ImageView.ScaleType.FIT_CENTER -> baseMatrix.setRectToRect(
                        mTempSrc,
                        mTempDst,
                        ScaleToFit.CENTER
                    )
                    ImageView.ScaleType.FIT_START -> baseMatrix.setRectToRect(
                        mTempSrc,
                        mTempDst,
                        ScaleToFit.START
                    )
                    ImageView.ScaleType.FIT_END -> baseMatrix.setRectToRect(
                        mTempSrc,
                        mTempDst,
                        ScaleToFit.END
                    )
                    ImageView.ScaleType.FIT_XY -> baseMatrix.setRectToRect(
                        mTempSrc,
                        mTempDst,
                        ScaleToFit.FILL
                    )
                    else -> {
                    }
                }
            }
        }
        resetMatrix()
    }

    private fun checkMatrixBounds(): Boolean {
        val rect = getDisplayRect(drawMatrix) ?: return false
        val height = rect.height()
        val width = rect.width()
        var deltaX = 0f
        var deltaY = 0f
        val viewHeight = getImageViewHeight(imageView)
        when {
            height <= viewHeight -> deltaY = when (scaleType) {
                ImageView.ScaleType.FIT_START -> -rect.top
                ImageView.ScaleType.FIT_END -> viewHeight.toFloat() - height - rect.top
                else -> (viewHeight - height) / 2 - rect.top
            }
            rect.top > 0 -> deltaY = -rect.top
            rect.bottom < viewHeight -> deltaY = viewHeight - rect.bottom
        }
        val viewWidth = getImageViewWidth(imageView)
        when {
            width <= viewWidth -> {
                deltaX = when (scaleType) {
                    ImageView.ScaleType.FIT_START -> -rect.left
                    ImageView.ScaleType.FIT_END -> viewWidth.toFloat() - width - rect.left
                    else -> (viewWidth - width) / 2 - rect.left
                }
                scrollEdge = EDGE_BOTH
            }
            rect.left > 0 -> {
                scrollEdge = EDGE_LEFT
                deltaX = -rect.left
            }
            rect.right < viewWidth -> {
                deltaX = viewWidth - rect.right
                scrollEdge = EDGE_RIGHT
            }
            else -> scrollEdge = EDGE_NONE
        }
        // Finally actually translate the matrix
        // Finally actually translate the matrix
        suppMatrix.postTranslate(deltaX, deltaY)
        return true
    }

    private fun getImageViewWidth(imageView: ImageView): Int {
        return imageView.width - imageView.paddingLeft - imageView.paddingRight
    }

    private fun getImageViewHeight(imageView: ImageView): Int {
        return imageView.height - imageView.paddingTop - imageView.paddingBottom
    }

    private fun cancelFling() {
        if (currentFlingRunnable != null) {
            currentFlingRunnable!!.cancelFling()
            currentFlingRunnable = null
        }
    }

    private inner class AnimatedZoomRunnable(
        private val zoomStart: Float,
        private val zoomEnd: Float,
        private val focalX: Float,
        private val focalY: Float,
    ) : Runnable {

        private val startTime: Long = System.currentTimeMillis()

        override fun run() {
            val t = interpolate()
            val scale = zoomStart + t * (zoomEnd - zoomStart)
            val deltaScale = scale / currentScale
            gestureListener.onScale(deltaScale, focalX, focalY)
            // We haven't hit our target scale yet, so post ourselves again
            if (t < 1f) {
                GestureCompat.postOnAnimation(imageView, this)
            }
        }

        private fun interpolate(): Float {
            var t = 1f * (System.currentTimeMillis() - startTime) / zoomDuration
            t = Math.min(1f, t)
            t = interpolator.getInterpolation(t)
            return t
        }
    }

    private inner class FlingRunnable(context: Context) : Runnable {

        private val scroller: OverScroller = OverScroller(context)
        private var currentX: Int = 0
        private var currentY: Int = 0

        fun cancelFling() {
            scroller.forceFinished(true)
        }

        fun fling(viewWidth: Int, viewHeight: Int, velocityX: Int, velocityY: Int) {
            val rect = displayRect ?: return
            val startX = Math.round(-rect.left)
            val minX: Int
            val maxX: Int
            val minY: Int
            val maxY: Int
            if (viewWidth < rect.width()) {
                minX = 0
                maxX = Math.round(rect.width() - viewWidth)
            } else {
                maxX = startX
                minX = maxX
            }
            val startY = Math.round(-rect.top)
            if (viewHeight < rect.height()) {
                minY = 0
                maxY = Math.round(rect.height() - viewHeight)
            } else {
                maxY = startY
                minY = maxY
            }
            currentX = startX
            currentY = startY
            // If we actually can move, fling the scroller
            if (startX != maxX || startY != maxY) {
                scroller.fling(
                    startX, startY, velocityX, velocityY, minX,
                    maxX, minY, maxY, 0, 0
                )
            }
        }

        override fun run() {
            if (scroller.isFinished) {
                return  // remaining post that should not be handled
            }
            if (scroller.computeScrollOffset()) {
                val newX = scroller.currX
                val newY = scroller.currY
                suppMatrix.postTranslate((currentX - newX).toFloat(), (currentY - newY).toFloat())
                checkAndDisplayMatrix()
                currentX = newX
                currentY = newY
                // Post On animation
                GestureCompat.postOnAnimation(imageView, this)
            }
        }
    }
}
