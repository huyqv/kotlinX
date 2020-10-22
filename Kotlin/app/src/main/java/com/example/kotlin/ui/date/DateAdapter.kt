package com.example.kotlin.ui.date

import android.view.View
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.R
import com.example.library.adapter.recycler.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.date_item.view.*
import java.lang.ref.WeakReference

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/10/09
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class DateAdapter : BaseRecyclerAdapter<Int>() {

    /**
     * [DateAdapter] override
     */
    override fun get(position: Int): Int? {
        return super.get(position % size)
    }

    override fun getItemCount(): Int {
        return size * 1000
    }

    override fun layoutResource(model: Int, position: Int): Int {
        return R.layout.date_item
    }

    override fun View.onBindModel(model: Int, position: Int, layout: Int) {
        dateTextView.text = model.toString()
    }

    /**
     * [BaseRecyclerAdapter] properties
     */
    val centerPosition: Int get() = itemCount / 2

    val centerValue: Int get() = get(centerPosition) ?: 1

    private val snapHelper = LinearSnapHelper()

    private var weakRecyclerView: WeakReference<RecyclerView> = WeakReference(null)

    val currentValue: Int
        get() {
            val recyclerView = weakRecyclerView.get() ?: return 0
            val view = snapHelper.findSnapView(recyclerView.layoutManager) ?: return 0
            val position = recyclerView.getChildAdapterPosition(view)
            return get(position) ?: return 0
        }

    fun snap(view: RecyclerView, onSnap: ((Int) -> Unit)? = null) {
        weakRecyclerView = WeakReference(view)
        this.bind(view)
        snapHelper.attachToRecyclerView(view)
        onSnap ?: return
        view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        val i = currentValue
                        if (i != 0) {
                            onSnap(i)
                        }
                    }
                }
            }
        })
    }

}