package com.example.kotlin.ui.selectable

import android.widget.TextView
import com.example.kotlin.base.ext.activityViewModel
import com.example.kotlin.base.ext.navResultLiveData
import com.example.kotlin.base.view.BaseFragment

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/10/22
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class SelectableSample {

    var selectedPosition: Int = -1

    fun <T> TextView.buildListItem(fragment: BaseFragment, data: List<T>?, block: SelectableAdapter<T>.() -> Unit) {
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
                ?.observe(fragment.viewLifecycleOwner) {
                    selectedPosition = -1
                    println("selected position: $it")
                }
        setOnClickListener {
            fragment.activityViewModel(SelectableVM::class).arg = arg
            //fragment.navigate(MainDirections.actionGlobalSelectableFragment())
        }
    }

}