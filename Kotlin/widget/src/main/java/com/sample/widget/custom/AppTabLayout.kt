package com.sample.widget.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.children
import com.sample.widget.R
import com.sample.widget.extension.*

class AppTabLayout : ConstraintLayout {

    private var selectionBarView: View? = null

    var onCheckedChanged: ((TextView) -> Unit)? = null

    var selectedViewId: Int = -1

    private val checkedChangeListener: ViewClickListener by lazy {
        object : ViewClickListener() {
            override fun onClicks(v: View?) {
                val selectedView = v as? TextView ?: return
                if (selectedView.id == selectedViewId) return
                selectedViewId = selectedView.id
                selectedView.textColor(R.color.colorBlack)
                updateTabSelectView(selectedView)
                onCheckedChanged?.invoke(selectedView)
                this@AppTabLayout.children.iterator().forEach { unSelectedView ->
                    if (unSelectedView is TextView && unSelectedView.id != selectedViewId) {
                        unSelectedView.textColor(android.R.color.darker_gray)
                    }
                }
            }
        }
    }

    constructor(context: Context, attributes: AttributeSet? = null) : super(context, attributes) {
        //val checkedButtonId = attributes?.getAttributeIntValue("android", "checkedButton", 0)
        addTabSelectionBar()
    }

    override fun onViewAdded(view: View?) {
        super.onViewAdded(view)
        (view as? TextView)?.also { radioButton ->
            radioButton.setOnClickListener(checkedChangeListener)
        }
    }

    private fun updateTabSelectView(selectedView: View) {
        val sId = selectionBarView?.id ?: return
        val height = dpToPx(2F).toInt()
        val extraWidth = dpToPx(2F).toInt()
        val width = selectedView.width + extraWidth
        beginTransition(400) {
            constrainWidth(sId, width)
            constrainDefaultWidth(sId, width)
            constrainHeight(sId, height)
            constrainDefaultHeight(sId, height)
            setTranslationZ(sId, 1F)
            connect(sId, ConstraintSet.START, selectedView.id, ConstraintSet.START)
            connect(sId, ConstraintSet.BOTTOM, selectedView.id, ConstraintSet.BOTTOM)
        }
    }

    private fun addTabSelectionBar() {
        selectionBarView = View(context).also {
            it.setBackgroundResource(R.color.colorPrimary)
            it.id = View.generateViewId()
        }
        this.addView(selectionBarView)
        val height = dpToPx(2F).toInt()
        val sId = selectionBarView!!.id
        editConstraint {
            constrainWidth(sId, 1)
            constrainDefaultWidth(sId, 1)
            constrainHeight(sId, height)
            constrainDefaultHeight(sId, height)
            setTranslationZ(sId, 1F)
            connect(sId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(sId, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        }
    }

}