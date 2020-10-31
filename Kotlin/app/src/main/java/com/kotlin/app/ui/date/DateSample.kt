package com.kotlin.app.ui.date

import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.library.extension.activityVM
import com.example.library.extension.navResultLiveData
import com.example.library.ui.BaseFragment
import java.text.SimpleDateFormat

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/10/22
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
                ?.observe(fragment.viewLifecycleOwner, Observer<String> {
                    selectedDate = it
                    this.text = it
                })
        this.setOnClickListener {
            fragment.activityVM(DateVM::class).arg = arg
            fragment.findNavController() //.navigate(R.id.)
        }
    }

}