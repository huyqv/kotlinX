package com.sample.library.zoom

import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView

class GestureHelper {

    interface GestureListener {

        fun onDrag(dx: Float, dy: Float)

        fun onFling(startX: Float, startY: Float, velocityX: Float, velocityY: Float)

        fun onScale(scaleFactor: Float, focusX: Float, focusY: Float)
    }

    interface MatrixChangedListener {

        /**
         * Callback for when the Matrix displaying the Drawable has changed. This could be because
         * the View's bounds have changed, or the user has zoomed.
         *
         * @param rect - Rectangle displaying the Drawable's new bounds.
         */
        fun onMatrixChanged(rect: RectF)
    }

    /**
     * Interface definition for callback to be invoked when attached ImageView currentScale changes
     */
    interface ScaleChangedListener {

        /**
         * Callback for when the currentScale changes
         *
         * @param scaleFactor the currentScale factor (less than 1 for zoom out, greater than 1 for zoom in)
         * @param focusX      focal point X position
         * @param focusY      focal point Y position
         */
        fun onScaleChange(scaleFactor: Float, focusX: Float, focusY: Float)
    }

    /**
     * Callback when the user tapped outside of the photo
     */
    interface OutsidePhotoTapListener {
        fun onOutsidePhotoTap(imageView: ImageView)
    }

    /**
     * A callback to be invoked when the Photo is tapped with a singletap.
     */
    interface PhotoTapListener {

        /**
         * A callback to receive where the user taps on a photo. You will only receive a callback if
         * the user taps on the actual photo, tapping on 'whitespace' will be ignored.
         *
         * @param view ImageView the user tapped.
         * @param x    where the user tapped from the of the Drawable, as percentage of the
         * Drawable width.
         * @param y    where the user tapped from the top of the Drawable, as percentage of the
         * Drawable height.
         */
        fun onPhotoTap(view: ImageView, x: Float, y: Float)
    }

    /**
     * A callback to be invoked when the ImageView is flung with a single
     * touch
     */
    interface SingleFlingListener {

        /**
         * A callback to receive where the user flings on a ImageView. You will receive a callback if
         * the user flings anywhere on the view.
         *
         * @param e1        MotionEvent the user first touch.
         * @param e2        MotionEvent the user last touch.
         * @param velocityX distance of user's horizontal fling.
         * @param velocityY distance of user's vertical fling.
         */
        fun onSingleFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
        ): Boolean
    }

    /**
     * Interface definition for a callback to be invoked when the photo is experiencing a drag event
     */
    interface ViewDragListener {

        /**
         * Callback for when the photo is experiencing a drag event. This cannot be invoked when the
         * user is scaling.
         *
         * @param dx The change of the coordinates in the x-direction
         * @param dy The change of the coordinates in the y-direction
         */
        fun onDrag(dx: Float, dy: Float)
    }

    interface ViewTapListener {

        /**
         * A callback to receive where the user taps on a ImageView. You will receive a callback if
         * the user taps anywhere on the view, tapping on 'whitespace' will not be ignored.
         *
         * @param view - View the user tapped.
         * @param x    - where the user tapped from the left of the View.
         * @param y    - where the user tapped from the top of the View.
         */
        fun onViewTap(view: View, x: Float, y: Float)
    }
}
