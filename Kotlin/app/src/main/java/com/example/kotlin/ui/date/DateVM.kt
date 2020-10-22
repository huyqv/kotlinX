package com.example.kotlin.ui.date

import androidx.lifecycle.MutableLiveData
import com.example.kotlin.base.vm.BaseViewModel
import com.example.library.extension.SHORT_FORMAT
import com.example.library.extension.timeFormat
import java.util.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/10/09
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class DateVM : BaseViewModel() {

    val argLiveData = MutableLiveData<DateArg>()

    var arg: DateArg?
        get() = argLiveData.value
        set(value) {
            argLiveData.postValue(value)
        }

    val daysLiveData = MutableLiveData<List<Int>>()

    val yearList: List<Int> get() = IntRange(1970, 2020).toList()

    val monthList: List<Int> get() = IntRange(1, 12).toList()

    var selectedYear: Int = 0

    var selectedMonth: Int = 0

    fun updateDayList(arg: DateArg) {
        val cal = Calendar.getInstance()
        try {
            val selectedDate = arg.selectedDate
            if (!selectedDate.isNullOrEmpty()) arg.format.parse(selectedDate)?.also {
                cal.time = it
            }
        } catch (ignore: Exception) {

        }
        updateDayList(cal)
    }

    fun updateDayList(year: Int, month: Int) {
        updateDayList(Calendar.getInstance().also {
            it.set(year, month - 1, 1)
        })
    }

    fun getCurrentDate(y: Int, m: Int, d: Int): String {
        val cal = Calendar.getInstance()
        cal.set(y, m - 1, d)
        return cal.timeFormat(argLiveData.value?.format ?: SHORT_FORMAT)
    }

    private fun updateDayList(calendar: Calendar) {
        selectedYear = calendar.get(Calendar.YEAR)
        selectedMonth = calendar.get(Calendar.MONTH) + 1
        val daysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        daysLiveData.value = IntRange(1, daysOfMonth).toList()
    }

}
