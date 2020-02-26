package com.huy.kotlin.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.huy.kotlin.R
import com.huy.library.extension.addOnClickListener
import com.huy.library.extension.toArray
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/18
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BaseJsonAdapter<T : JsonElement> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * [BaseJsonAdapter] abstract function for initialize recycler view type.
     */
    @LayoutRes
    protected abstract fun layoutResource(json: T, position: Int): Int

    protected abstract fun View.onBindModel(json: T, position: Int, @LayoutRes layout: Int)

    open fun View.onFirstBindModel(model: T, position: Int, @LayoutRes layout: Int) {
        onBindModel(model, position, layout)
    }

    /**
     * [RecyclerView.Adapter] override.
     */
    override fun getItemCount(): Int {

        return if (blankLayoutResource != 0 || footerLayoutResource != 0) sizeCache + 1
        else sizeCache
    }

    override fun getItemViewType(position: Int): Int {

        if (dataIsEmpty() && blankLayoutResource != 0) return blankLayoutResource

        if (dataNotEmpty() && footerLayoutResource != 0 && position == sizeCache) return footerLayoutResource

        val model = get(position) ?: return R.layout.view_gone

        return layoutResource(model, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val type = getItemViewType(position)

        if (type == R.layout.view_gone) return

        if (type == blankLayoutResource) {
            blankItemVisible?.also { it(viewHolder.itemView) }
            return
        }

        if (type == footerLayoutResource) {
            if (position.isNotIndexed()) footerIndexChange?.also { it(viewHolder.itemView, position) }
            return
        }

        val model = get(position) ?: return

        if (position.isNotIndexed()) viewHolder.itemView.onBindModel(model, position, type)
        else viewHolder.itemView.onFirstBindModel(model, position, type)

        position.updateLastIndex()

        viewHolder.itemView.addOnClickListener {
            itemClick?.also { it(model, position) }
        }

        viewHolder.itemView.setOnLongClickListener {
            itemLongClick?.also { it(model, position) }
            return@setOnLongClickListener true
        }

    }

    /**
     * Layout resource for empty data.
     */
    @LayoutRes
    open var blankLayoutResource: Int = 0

    /**
     * Layout resource for footer item.
     */
    @LayoutRes
    open var footerLayoutResource: Int = 0

    open fun showFooter(@LayoutRes res: Int) {
        footerLayoutResource = res
        notifyItemChanged(sizeCache)
    }

    open fun hideFooter() {
        footerLayoutResource = 0
        notifyItemChanged(sizeCache)
    }

    /**
     * User interfaces.
     */
    private var itemClick: ((T, Int) -> Unit)? = null

    open fun onItemClick(block: (T, Int) -> Unit) {
        itemClick = block
    }

    private var itemLongClick: ((T, Int) -> Unit)? = null

    open fun onItemLongClick(block: (T, Int) -> Unit) {
        itemLongClick = block
    }

    private var footerIndexChange: ((View, Int) -> Unit)? = null

    open fun onFooterIndexChange(block: ((View, Int) -> Unit)) {
        footerIndexChange = block
    }

    private var blankItemVisible: ((View) -> Unit)? = null

    open fun onBlankItemVisible(block: ((View) -> Unit)) {
        blankItemVisible = block
    }

    private var lastIndexed: Int = -1

    private fun Int.isNotIndexed(): Boolean {
        return this > lastIndexed
    }

    private fun Int.updateLastIndex() {
        if (this > lastIndexed) lastIndexed = this
    }

    /**
     * Data list handle.
     */
    var data: JsonArray? = null

    var sizeCache = 0

    open fun indexInBound(position: Int): Boolean {
        return position > -1 && position < sizeCache
    }

    open fun get(position: Int): T? {
        if (data == null) return null
        @Suppress("UNCHECKED_CAST")
        if (indexInBound(position)) return data!![position] as T
        return null
    }

    open fun resize() {
        sizeCache = data?.size() ?: 0
    }

    open fun dataIsEmpty(): Boolean {
        return sizeCache == 0
    }

    open fun dataNotEmpty(): Boolean {
        return sizeCache != 0
    }

    open fun lastPosition(): Int {
        return if (dataIsEmpty()) -1 else (sizeCache - 1)
    }

    open fun set(s: String?) {
        if (s.isNullOrEmpty()) {
            newData()
            notifyDataChanged()
            return
        }
        Observable.just(s.toArray())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { set(it) }
    }

    open fun set(array: JsonArray?) {
        data = array
        notifyDataChanged()
    }

    open fun set(obj: JsonObject?) {
        if (obj == null) newData()
        else newData().add(obj)
        notifyDataChanged()
    }

    open fun setElseEmpty(s: String?) {

        if (s.isNullOrEmpty()) return

        Observable.just(s.toArray())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { setElseEmpty(it) }
    }

    open fun setElseEmpty(array: JsonArray?) {
        array ?: return
        if (array.size() == 0) return
        data = array
        notifyDataChanged()
    }

    open fun setElseEmpty(obj: JsonObject?) {
        obj ?: return
        newData().add(obj)
        notifyDataChanged()
    }

    open fun add(s: String?) {
        if (s.isNullOrEmpty()) return
        Observable.just(s.toArray())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { add(it) }
    }

    open fun add(array: JsonArray?) {
        array ?: return
        if (array.size() == 0) return
        data().addAll(array)
        notifyRangeChanged()
    }

    open fun add(obj: JsonObject?) {
        obj ?: return
        data().add(obj)
        notifyRangeChanged()
    }

    open fun edit(index: Int, model: T?) {
        model ?: return
        if (indexInBound(index)) {
            data()[index] = model
            notifyItemChanged(index)
        }
    }

    open fun remove(index: Int) {
        if (indexInBound(index)) {
            data().remove(index)
            notifyDataChanged()
        }
    }

    open fun remove(model: T?) {
        model ?: return
        val index = data().indexOf(model)
        if (indexInBound(index)) {
            data().remove(model)
            notifyDataChanged()
        }
    }

    open fun clear() {
        data = null
        notifyDataChanged()
    }

    open fun unBind() {
        data = JsonArray()
        notifyDataChanged()
    }

    open fun notifyDataChanged() {
        resize()
        notifyDataSetChanged()
    }

    open fun notifyRangeChanged() {
        val s = sizeCache
        resize()
        notifyItemRangeChanged(s, sizeCache + 1)
    }

    open fun data(): JsonArray {
        if (data == null) data = JsonArray()
        return data!!
    }

    open fun newData(): JsonArray {
        data = JsonArray()
        return data!!
    }

    open fun bind(recyclerView: RecyclerView, block: (LinearLayoutManager.() -> Unit)? = null) {

        val layoutManager = LinearLayoutManager(recyclerView.context)
        block?.let { layoutManager.block() }
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = this
    }

    open fun bind(recyclerView: RecyclerView, spanCount: Int, includeEdge: Boolean = true, block: (GridLayoutManager.() -> Unit)? = null) {

        val layoutManager = GridLayoutManager(recyclerView.context, spanCount)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (dataIsEmpty() || position == sizeCache) layoutManager.spanCount
                else 1
            }
        }
        block?.let { layoutManager.block() }
        recyclerView.layoutManager = layoutManager
        GridDecoration.draw(recyclerView, layoutManager.spanCount, 0, includeEdge)
        recyclerView.adapter = this
    }

}


