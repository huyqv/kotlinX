package com.sample.widget.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout

open class AppBarLayout constructor(context: Context, attrs: AttributeSet? = null) :
        com.google.android.material.appbar.AppBarLayout(context, attrs) {

    val behavior: Behavior?
        get() {
            val behavior = (this.layoutParams as? CoordinatorLayout.LayoutParams)?.behavior
            return behavior as? Behavior
        }

    var isExpandable: Boolean
        get() = behavior?.isExpandable ?: false
        set(value) {
            behavior?.isExpandable = value
        }

    /**
     * <wee.digital.widget.base.AppBarLayout
     *      xmlns:android="http://schemas.android.com/apk/res/android"
     *      xmlns:app="http://schemas.android.com/apk/res-auto"
     *      xmlns:tools="http://schemas.android.com/tools"
     *      android:id="@+id/appBarLayout"
     *      android:layout_width="match_parent"
     *      android:layout_height="wrap_content"
     *      android:background="@color/colorTransparent"
     *      android:minHeight="0dp"
     *      app:layout_behavior="wee.digital.widget.base.AppBarLayout.Behavior"
     *      android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"
     *      app:elevation="0dp">
     * </wee.digital.widget.base.AppBarLayout>
     */
    class Behavior : com.google.android.material.appbar.AppBarLayout.Behavior {

        var isExpandable = true

        constructor(context: Context?, attrs: AttributeSet? = null) : super(context, attrs)

        override fun onStartNestedScroll(parent: CoordinatorLayout, child: com.google.android.material.appbar.AppBarLayout, directTargetChild: View, target: View, nestedScrollAxes: Int, type: Int): Boolean {
            return isExpandable && super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type)
        }
    }
}