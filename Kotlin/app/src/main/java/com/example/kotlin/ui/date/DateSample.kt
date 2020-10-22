package com.example.kotlin.ui.date

import android.widget.TextView
import com.example.kotlin.base.ext.activityViewModel
import com.example.kotlin.base.ext.navResultLiveData
import com.example.kotlin.base.view.BaseFragment
import java.text.SimpleDateFormat

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/10/22
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class DateSample {

    var selectedDate: String? = null

    fun TextView.buildDatePicker(fragment: BaseFragment, format: SimpleDateFormat = SimpleDateFormat("yyyy/MM/ddd")) {
        val arg = DateArg(
                key = this.id.toString(),
                format = format,
                selectedDate = selectedDate
        )
        fragment.navResultLiveData<String>(arg.key)
                ?.observe(fragment.viewLifecycleOwner) {
                    selectedDate = it
                    text = it
                }
        setOnClickListener {
            fragment.activityViewModel(DateVM::class).arg = arg
            //fragment.navigate(MainDirections.actionGlobalDateFragment())
        }
    }

}