package com.example.library.zoom

import android.content.Context
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.VelocityTracker
import android.view.ViewConfiguration

/**
 * Does a whole lot of gesture detecting.
 */
class GestureDetector internal constructor(context: Context, private val listener: GestureHelper.GestureListener) {

    companion object {
        private const val INVALID_POINTER_ID = -1
    }

    private val detector: ScaleGestureDetector

    private val touchSlop: Float

    private val minimumVelocity: Float

    private var activePointerId = INVALID_POINTER_ID

    private var activePointerIndex = 0

    private var velocityTracker: VelocityTracker? = null

    private var lastTouchX: Float = 0.toFloat()

    private var lastTouchY: Float = 0.toFloat()

    val isScaling: Boolean
        get() = detector.isInProgress

    var isDragging: Boolean = false
        private set

    init {
        val configuration = ViewConfiguration.get(context)
        minimumVelocity = configuration.scaledMinimumFlingVelocity.toFloat()
        touchSlop = configuration.scaledTouchSlop.toFloat()
        val scaleListener = object : ScaleGestureDetector.OnScaleGestureListener {

            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val scaleFactor = detector.scaleFactor

                if (scaleFactor.isNaN() || scaleFactor.isInfinite())
                    return false

                listener.onScale(scaleFactor,
                        detector.focusX, detector.focusY)
                return true
            }

            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                return true
            }

            override fun onScaleEnd(detector: ScaleGestureDetector) {
                // NO-OP
            }
        }
        detector = ScaleGestureDetector(context, scaleListener)
    }

    private fun getActiveX(ev: MotionEvent): Float {
        return try {
            ev.getX(activePointerIndex)
        } catch (e: Exception) {
            ev.x
        }
    }

    private fun getActiveY(ev: MotionEvent): Float {
        return try {
            ev.getY(activePointerIndex)
        } catch (e: Exception) {
            ev.y
        }
    }

    fun onTouchEvent(ev: MotionEvent): Boolean {
        return try {
            detector.onTouchEvent(ev)
            processTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            // Fix for support lib bug, happening when onDestroy is called
            true
        }
    }

    private fun processTouchEvent(ev: MotionEvent): Boolean {

        val action = ev.action

        when (action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                activePointerId = ev.getPointerId(0)

                velocityTracker = VelocityTracker.obtain()
                if (null != velocityTracker) {
                    velocityTracker!!.addMovement(ev)
                }

                lastTouchX = getActiveX(ev)
                lastTouchY = getActiveY(ev)
                isDragging = false
            }
            MotionEvent.ACTION_MOVE -> {
                val x = getActiveX(ev)
                val y = getActiveY(ev)
                val dx = x - lastTouchX
                val dy = y - lastTouchY

                if (!isDragging) {
                    // Use Pythagoras to see if drag length is larger than
                    // touch slop
                    isDragging = Math.sqrt((dx * dx + dy * dy).toDouble()) >= touchSlop
                }

                if (isDragging) {
                    listener.onDrag(dx, dy)
                    lastTouchX = x
                    lastTouchY = y

                    if (null != velocityTracker) {
                        velocityTracker!!.addMovement(ev)
                    }
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                activePointerId = INVALID_POINTER_ID
                // Recycle Velocity Tracker
                if (null != velocityTracker) {
                    velocityTracker!!.recycle()
                    velocityTracker = null
                }
            }
            MotionEvent.ACTION_UP -> {
                activePointerId = INVALID_POINTER_ID
                if (isDragging) {
                    if (null != velocityTracker) {
                        lastTouchX = getActiveX(ev)
                        lastTouchY = getActiveY(ev)

                        // Compute velocity within the last 1000ms
                        velocityTracker!!.addMovement(ev)
                        velocityTracker!!.computeCurrentVelocity(1000)

                        val vX = velocityTracker!!.xVelocity
                        val vY = velocityTracker!!
                                .yVelocity

                        // If the velocity is greater than minVelocity, call
                        // listener
                        if (Math.max(Math.abs(vX), Math.abs(vY)) >= minimumVelocity) {
                            listener.onFling(lastTouchX, lastTouchY, -vX,
                                    -vY)
                        }
                    }
                }

                // Recycle Velocity Tracker
                if (null != velocityTracker) {
                    velocityTracker!!.recycle()
                    velocityTracker = null
                }
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex = GestureUtil.getPointerIndex(ev.action)
                val pointerId = ev.getPointerId(pointerIndex)
                if (pointerId == activePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    val newPointerIndex = if (pointerIndex == 0) 1 else 0
                    activePointerId = ev.getPointerId(newPointerIndex)
                    lastTouchX = ev.getX(newPointerIndex)
                    lastTouchY = ev.getY(newPointerIndex)
                }
            }
        }

        activePointerIndex = ev.findPointerIndex(if (activePointerId != INVALID_POINTER_ID)
            activePointerId else 0)
        return true
    }


}
