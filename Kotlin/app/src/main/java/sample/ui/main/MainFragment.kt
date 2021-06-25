package sample.ui.main

import android.view.LayoutInflater
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.example.library.extension.activityVM
import com.example.library.extension.addViewClickListener
import com.kotlin.app.ui.base.BaseFragment
import sample.ui.date.DateArg
import sample.ui.selectable.SelectableAdapter
import sample.ui.selectable.SelectableArg
import java.text.SimpleDateFormat

abstract class MainFragment<B : ViewBinding>(block: (LayoutInflater) -> B) :
        BaseFragment<B>(block), MainView {

    override val mainActivity get() = requireActivity() as? MainActivity

    override val mainVM by activityVM(MainVM::class)

    override val dialogVM by activityVM(DialogVM::class)

    fun buildDatePicker(view: TextView, builder: (DateArg) -> Unit) {
        val arg = DateArg(
                key = view.id.toString(),
                format = SimpleDateFormat("yyyy/MM/dd"),
                selectedDate = view.text?.toString()
        )
        builder(arg)
        navResultLiveData<String>(arg.key)?.observe {
            view.text = it
        }
        view.addViewClickListener {
            // activityVM(DialogVM::class).dateLiveData.value = arg
            //navigate(MainDirections.actionGlobalDateFragment())
        }
    }

    fun <T> buildListItem(view: TextView, data: List<T>?, block: SelectableAdapter<T>.() -> Unit) {
        data ?: return
        val adapter = SelectableAdapter<T>().also {
            it.selectedPosition = -1
            it.block()
            it.set(data)
        }
        val arg = SelectableArg(
                key = view.id.toString(),
                isSearchable = false,
                title = "Select a string",
                adapter = adapter
        )
        navResultLiveData<Int>(arg.key)?.observe {
            println("selected position: $it")
        }

        view.addViewClickListener {
            //this.activityVM(DialogVM::class).selectableLiveData.value = arg
            //this.navigate(MainDirections.actionGlobalSelectableFragment())
        }
    }
}