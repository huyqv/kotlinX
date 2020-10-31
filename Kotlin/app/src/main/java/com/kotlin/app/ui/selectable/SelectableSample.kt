package com.kotlin.app.ui.selectable

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.library.extension.activityVM
import com.example.library.extension.navResultLiveData

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/10/22
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class SelectableSample {

    var selectedPosition: Int = -1

    fun <T> TextView.buildListItem(fragment: Fragment, data: List<T>?, block: SelectableAdapter<T>.() -> Unit) {
        data ?: return
        val adapter = SelectableAdapter<T>().also {
            it.selectedPosition = -1
            it.block()
            it.set(data)
        }
        val arg = SelectableArg(
                key = this.id.toString(),
                isSearchable = false,
                title = "Select a string",
                adapter = adapter
        )
        fragment.navResultLiveData<Int>(arg.key)
                ?.observe(fragment.viewLifecycleOwner, Observer<Int> {
                    selectedPosition = -1
                    println("selected position: $it")
                })

        setOnClickListener {
            fragment.activityVM(SelectableVM::class).arg = arg
            //fragment.navigate(MainDirections.actionGlobalSelectableFragment())
        }
    }

}