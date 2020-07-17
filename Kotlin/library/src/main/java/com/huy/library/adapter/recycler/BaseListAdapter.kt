package com.huy.library.adapter.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.*
import com.huy.library.extension.addViewClickListener

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/18
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BaseListAdapter<T> : ListAdapter<T, RecyclerView.ViewHolder> {


    private val differ: AsyncListDiffer<T>

    constructor(itemCallback: DiffUtil.ItemCallback<T> = DiffItemCallback()) : super(itemCallback) {
        differ = asyncListDiffer(itemCallback)
    }


    /**
     * [RecyclerView.Adapter] override.
     */
    override fun getItemCount(): Int {
        return if (blankLayoutResource != 0 || footerLayoutResource != 0) size + 1
        else size
    }

    override fun getItemViewType(position: Int): Int {

        if (dataIsEmpty && blankLayoutResource != 0) return blankLayoutResource

        if (dataNotEmpty && footerLayoutResource != 0 && position == size) return footerLayoutResource

        val model = get(position) ?: return 0

        return layoutResource(model, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = if (viewType == 0) {
            View(parent.context).apply { visibility = View.GONE }
        } else {
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        }
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {

        val type = getItemViewType(position)

        if (type == 0) return

        if (type == blankLayoutResource) {
            return
        }

        if (type == footerLayoutResource) {
            if (position.isNotIndexed) onFooterIndexChange(viewHolder.itemView, position)
            return
        }

        val model = get(position) ?: return

        if (position.isNotIndexed) viewHolder.itemView.onFirstBindModel(model, position, type)
        else viewHolder.itemView.onBindModel(model, position, type)

        position.updateLastIndex()

        viewHolder.itemView.addViewClickListener {
            onItemClick(model, position)
        }

        viewHolder.itemView.setOnLongClickListener {
            onItemLongClick(model, position)
            return@setOnLongClickListener true
        }

    }

    override fun getCurrentList(): MutableList<T> {
        return differ.currentList
    }

    override fun submitList(list: MutableList<T>?) {
        differ.submitList(list)
    }

    override fun submitList(list: MutableList<T>?, commitCallback: Runnable?) {
        differ.submitList(list, commitCallback)
    }


    /**
     * [BaseRecyclerAdapter] abstractions
     */
    @LayoutRes
    protected abstract fun layoutResource(model: T, position: Int): Int

    protected abstract fun View.onBindModel(model: T, position: Int, @LayoutRes layout: Int)

    open fun View.onFirstBindModel(model: T, position: Int, @LayoutRes layout: Int) {
        onBindModel(model, position, layout)
    }

    open fun onCreateItemView(parent: ViewGroup, viewType: Int): View {
        return if (viewType == 0) {
            View(parent.context).apply { visibility = View.GONE }
        } else {
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        }
    }


    /**
     * Layout resource for empty data.
     */
    @LayoutRes
    protected open var blankLayoutResource: Int = 0


    /**
     * Layout resource for footer item.
     */
    @LayoutRes
    open var footerLayoutResource: Int = 0

    var onFooterIndexChange: (View, Int) -> Unit = { _, _ -> }

    fun showFooter(@LayoutRes res: Int) {
        footerLayoutResource = res
        notifyItemChanged(size)
    }

    fun hideFooter() {
        footerLayoutResource = 0
        notifyItemChanged(size)
    }


    /**
     * User interfaces.
     */
    var onItemClick: (T, Int) -> Unit = { _, _ -> }

    var onItemLongClick: (T, Int) -> Unit = { _, _ -> }


    /**
     * Position
     */
    private var lastIndexPosition: Int = -1

    private fun Int.updateLastIndex() {
        if (this > lastIndexPosition) lastIndexPosition = this
    }

    private val Int.isNotIndexed: Boolean get() = this > lastIndexPosition

    private val Int.indexInBound: Boolean get() = this > -1 && this < size

    private val Int.indexOutBound: Boolean get() = this < 0 || this >= size


    /**
     * Data list
     */
    val size: Int get() = currentList.size

    val dataIsEmpty: Boolean get() = currentList.isEmpty()

    val dataNotEmpty: Boolean get() = currentList.isNotEmpty()

    val lastPosition: Int get() = if (currentList.isEmpty()) -1 else (currentList.size - 1)


    /**
     * Data update
     */
    open fun submit() {
        set(currentList)
    }

    open fun get(position: Int): T? {
        if (position.indexInBound) return currentList[position]
        return null
    }

    open fun set(collection: Collection<T>?) {
        lastIndexPosition = -1
        submitList(if (collection != null) ArrayList(collection) else null)
    }

    open fun set(list: MutableList<T>?) {
        lastIndexPosition = -1
        submitList(if (list != null) ArrayList(list) else null)
    }

    open fun set(array: Array<T>?) {
        lastIndexPosition = -1
        submitList(array?.toMutableList())
    }

    open fun set(model: T?) {
        lastIndexPosition = -1
        submitList(if (model != null) mutableListOf(model) else null)
    }

    open fun setElseEmpty(collection: Collection<T>?) {
        if (collection.isNullOrEmpty()) return
        submitList(ArrayList(collection))
    }

    open fun setElseEmpty(list: MutableList<T>?) {
        if (list.isNullOrEmpty()) return
        submitList(ArrayList(list))
    }

    open fun setElseEmpty(array: Array<T>?) {
        if (array.isNullOrEmpty()) return
        submitList(array.toMutableList())
    }

    open fun setElseEmpty(model: T?) {
        model ?: return
        submitList(mutableListOf(model))
    }

    open fun add(collection: Collection<T>?) {
        if (collection.isNullOrEmpty()) return
        currentList.addAll(collection)
        submit()
    }

    open fun add(array: Array<T>?) {
        if (array == null || array.isEmpty()) return
        currentList.addAll(array)
        submit()
    }

    open fun add(model: T?) {
        model ?: return
        currentList.add(model)
        submit()
    }

    open fun addFirst(model: T?) {
        model ?: return
        currentList.add(0, model)
        submit()
    }

    open fun edit(index: Int, model: T?) {
        model ?: return
        if (index.indexInBound) {
            currentList[index] = model
            submit()
        }
    }

    open fun remove(index: Int) {
        currentList.removeAt(index)
        submit()
    }

    open fun remove(model: T?) {
        model ?: return
        val index = currentList.indexOf(model)
        if (index.indexInBound) {
            currentList.remove(model)
        }
    }


    /**
     * Binding
     */
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
                return if (dataIsEmpty || position == size) layoutManager.spanCount
                else 1
            }
        }
        block?.let { layoutManager.block() }
        recyclerView.layoutManager = layoutManager
        GridDecoration.draw(recyclerView, layoutManager.spanCount, 0, includeEdge)
        recyclerView.adapter = this
    }


    /**
     * Utils
     */
    private fun asyncListDiffer(itemCallback: DiffUtil.ItemCallback<T>): AsyncListDiffer<T> {

        val adapterCallback = AdapterListUpdateCallback(this)
        val listCallback = object : ListUpdateCallback {
            override fun onChanged(position: Int, count: Int, payload: Any?) {
                adapterCallback.onChanged(position + 1, count, payload)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                adapterCallback.onMoved(fromPosition + 1, toPosition + 1)
            }

            override fun onInserted(position: Int, count: Int) {
                adapterCallback.onInserted(position + 1, count + 1)
            }

            override fun onRemoved(position: Int, count: Int) {
                adapterCallback.onRemoved(position + 1, count)
            }
        }
        return AsyncListDiffer<T>(listCallback, AsyncDifferConfig.Builder<T>(itemCallback).build())
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

}