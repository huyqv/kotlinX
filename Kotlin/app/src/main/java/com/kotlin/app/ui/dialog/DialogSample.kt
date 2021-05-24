package com.kotlin.app.ui.dialog

import android.widget.TextView
import com.example.library.extension.addViewClickListener
import com.example.library.extension.navResultLiveData
import com.kotlin.app.MainDirections
import com.kotlin.app.ui.dialog.date.DateArg
import com.kotlin.app.ui.dialog.selectable.SelectableAdapter
import com.kotlin.app.ui.dialog.selectable.SelectableArg
import com.kotlin.app.ui.main.MainFragment
import java.text.SimpleDateFormat

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2021/05/24
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun MainFragment.buildDatePicker(view: TextView, builder: (DateArg) -> Unit) {
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
        activityVM(DialogVM::class).dateLiveData.value = arg
        navigate(MainDirections.actionGlobalDateFragment())
    }
}

fun <T> MainFragment.buildListItem(view: TextView, data: List<T>?, block: SelectableAdapter<T>.() -> Unit) {
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
        this.activityVM(DialogVM::class).selectableLiveData.value = arg
        this.navigate(MainDirections.actionGlobalSelectableFragment())
    }
}
