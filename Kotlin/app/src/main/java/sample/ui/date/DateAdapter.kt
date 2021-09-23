package sample.ui.date

import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.kotlin.app.R
import com.kotlin.app.databinding.DateItemBinding
import com.sample.library.adapter.BaseListAdapter
import com.sample.library.adapter.BaseRecyclerAdapter
import com.sample.library.adapter.ItemOptions
import java.lang.ref.WeakReference

class DateAdapter : BaseListAdapter<Int>() {

    /**
     * [BaseRecyclerAdapter] override
     */
    override fun get(position: Int): Int? {
        return super.get(position % size)
    }

    override fun getItemCount(): Int {
        return size * 1000
    }

    override fun modelItemOptions(item: Int, position: Int): ItemOptions? {
        return ItemOptions(R.layout.date_item, DateItemBinding::bind)
    }

    override fun ViewBinding.onBindModelItem(item: Int, position: Int) {
        (this as? DateItemBinding)?.apply {
            textViewDate.text = item.toString()
        }
    }

    /**
     * [DateAdapter] properties
     */
    val centerPosition: Int get() = itemCount / 2

    val centerValue: Int get() = get(centerPosition) ?: 1

    private val snapHelper = LinearSnapHelper()

    private var weakRecyclerView: WeakReference<RecyclerView?> = WeakReference(null)

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