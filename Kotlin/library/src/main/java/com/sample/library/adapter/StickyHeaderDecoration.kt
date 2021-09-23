package com.sample.library.adapter

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * A sticky header decoration for android's RecyclerView.
 * Currently only supports LinearLayoutManager in VERTICAL orientation.
 */
class StickyHeaderDecoration<T : RecyclerView.ViewHolder>(
    private val adapter: StickyHeaderAdapter<T>,
    renderInline: Boolean,
    sticky: Boolean
) : RecyclerView.ItemDecoration() {

    private val headerCache: MutableMap<Long, RecyclerView.ViewHolder>
    private val renderInline: Boolean
    private val sticky: Boolean

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        var headerHeight = 0
        if (position != RecyclerView.NO_POSITION && hasHeader(parent, adapter, position)) {
            val header = getHeader(parent, adapter, position).itemView
            headerHeight = getHeaderHeightForLayout(header)
        }
        outRect[0, headerHeight, 0] = 0
    }

    private fun hasHeader(
        parent: RecyclerView, adapter: StickyHeaderAdapter<T>, adapterPos: Int
    ): Boolean {
        val headerId = adapter.getHeaderId(adapterPos)
        if (headerId == StickyHeaderAdapter.NO_HEADER_ID) {
            return false
        }
        val isReverse = isReverseLayout(parent)
        val itemCount = (adapter as RecyclerView.Adapter<*>).itemCount
        if (isReverse && adapterPos == itemCount - 1 && adapter.getHeaderId(adapterPos) != -1L ||
            !isReverse && adapterPos == 0
        ) {
            return true
        }
        val previous = adapterPos + if (isReverse) 1 else -1
        val previousHeaderId = adapter.getHeaderId(previous)
        return previousHeaderId != StickyHeaderAdapter.NO_HEADER_ID && headerId != previousHeaderId
    }

    private fun getHeader(
        parent: RecyclerView, adapter: StickyHeaderAdapter<T>, position: Int
    ): RecyclerView.ViewHolder {
        val key = adapter.getHeaderId(position)
        var headerHolder = headerCache[key]
        if (headerHolder == null) {
            if (key != StickyHeaderAdapter.NO_HEADER_ID) {
                headerHolder = adapter.onCreateHeaderViewHolder(parent, position)
                adapter.onBindHeaderViewHolder(headerHolder, position)
            }
            if (headerHolder == null) {
                headerHolder = GoneViewHolder(parent)
            }
            headerCache[key] = headerHolder
        }
        val header = headerHolder.itemView
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec =
            View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)
        val childWidth = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight, header.layoutParams.width
        )
        val childHeight = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom, header.layoutParams.height
        )
        header.measure(childWidth, childHeight)
        header.layout(0, 0, header.measuredWidth, header.measuredHeight)
        return headerHolder
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val count = parent.childCount
        var start = 0
        for (layoutPos in 0 until count) {
            val child = parent.getChildAt(translatedChildPosition(parent, layoutPos))
            val adapterPos = parent.getChildAdapterPosition(child)
            val key = adapter.getHeaderId(adapterPos)
            if (key == StickyHeaderAdapter.NO_HEADER_ID) {
                start = layoutPos + 1
            }
            if (adapterPos != RecyclerView.NO_POSITION && (layoutPos == start && sticky || hasHeader(
                    parent,
                    adapter,
                    adapterPos
                ))
            ) {
                val header = getHeader(parent, adapter, adapterPos).itemView
                canvas.save()
                val left = child.left
                val top = getHeaderTop(parent, child, header, adapterPos, layoutPos)
                canvas.translate(left.toFloat(), top.toFloat())
                header.draw(canvas)
                canvas.restore()
            }
        }
    }

    private fun getHeaderTop(
        parent: RecyclerView, child: View, header: View, adapterPos: Int, layoutPos: Int
    ): Int {
        val headerHeight = getHeaderHeightForLayout(header)
        var top = getChildY(child) - headerHeight
        if (sticky && layoutPos == 0) {
            val count = parent.childCount
            val currentId = adapter.getHeaderId(adapterPos)
            // find next view with header and compute the offscreen push if needed
            for (i in 1 until count) {
                val adapterPosHere = parent.getChildAdapterPosition(
                    parent.getChildAt(
                        translatedChildPosition(
                            parent,
                            i
                        )
                    )
                )
                if (adapterPosHere != RecyclerView.NO_POSITION) {
                    val nextId = adapter.getHeaderId(adapterPosHere)
                    if (nextId != currentId) {
                        val next = parent.getChildAt(translatedChildPosition(parent, i))
                        val offset = getChildY(next) - (headerHeight + getHeader(
                            parent,
                            adapter,
                            adapterPosHere
                        ).itemView.height)
                        return if (offset < 0) {
                            offset
                        } else {
                            break
                        }
                    }
                }
            }
            if (sticky) top = 0.coerceAtLeast(top)
        }
        return top
    }

    private fun getHeaderHeightForLayout(header: View): Int {
        return if (renderInline) 0 else header.height
    }



    companion object {

        private fun translatedChildPosition(parent: RecyclerView, position: Int): Int {
            return if (isReverseLayout(parent)) parent.childCount - 1 - position else position
        }

        private fun getChildY(child: View): Int {
            return child.y.toInt()
        }

        private fun isReverseLayout(parent: RecyclerView): Boolean {
            return (parent.layoutManager as? LinearLayoutManager)?.reverseLayout ?: false
        }
    }

    init {
        headerCache = HashMap()
        this.renderInline = renderInline
        this.sticky = sticky
    }
}