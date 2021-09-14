package sample.ui.date

import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.kotlin.app.databinding.DateBinding
import com.sample.library.extension.timeFormat
import sample.ui.main.MainDialogFragment
import java.util.*
import kotlin.math.abs

class DateFragment : MainDialogFragment<DateBinding>() {

    private var arg: DateArg
        get() = dialogVM.dateLiveData.value!!
        set(value) {
            dialogVM.dateLiveData.value = value
        }

    override fun inflating(): (LayoutInflater) -> ViewBinding {
        return DateBinding::inflate
    }

    override fun onViewCreated() {
        addClickListener(bind.viewDone)
    }

    override fun onLiveDataObserve() {
        dialogVM.dateLiveData.observe {
            if (null != it) {
                updateDayList(it)
            } else {
                dismissAllowingStateLoss()
            }
        }
    }

    override fun onViewClick(v: View?) {
        when (v) {
            bind.viewDone -> {
                setNavResult(arg.key, currentDate)
                onBackPressed()
            }
        }
    }

    /**
     * [DateFragment] properties
     */
    private val yearAdapter = DateAdapter()

    private val monthAdapter = DateAdapter()

    private val dayAdapter = DateAdapter()

    private val currentDate: String
        get() {
            return getCurrentDate(
                yearAdapter.currentValue,
                monthAdapter.currentValue,
                dayAdapter.currentValue
            )
        }

    private val yearList: List<Int> get() = IntRange(1970, 2020).toList()

    private val monthList: List<Int> get() = IntRange(1, 12).toList()

    private var selectedYear: Int = 0

    private var selectedMonth: Int = 0

    private fun updateDayList(arg: DateArg) {
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

    private fun updateDayList(year: Int, month: Int) {
        updateDayList(Calendar.getInstance().also {
            it.set(year, month - 1, 1)
        })
    }

    private fun getCurrentDate(y: Int, m: Int, d: Int): String {
        val cal = Calendar.getInstance()
        cal.set(y, m - 1, d)
        return cal.timeFormat(arg.format)
    }

    private fun updateDayList(calendar: Calendar) {
        selectedYear = calendar.get(Calendar.YEAR)
        selectedMonth = calendar.get(Calendar.MONTH) + 1
        val daysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        if (bind.recyclerViewDay.adapter == null) {
            onBindDaysInit()
        }
        onBindDayList(IntRange(1, daysOfMonth).toList())
    }

    private fun onBindDaysInit() {
        onBindYearList()
        onBindMonthList()
        dayAdapter.snap(bind.recyclerViewDay)
    }

    private fun onBindYearList() {
        yearAdapter.also {
            it.set(yearList)
            it.snap(bind.recyclerViewYear) { year ->
                updateDayList(year, monthAdapter.currentValue)
            }
            val correctPosition = abs(selectedYear - it.centerValue) + it.centerPosition - 1
            bind.recyclerViewYear.scrollToPosition(correctPosition)
        }
    }

    private fun onBindMonthList() {
        monthAdapter.also {
            it.set(monthList)
            it.snap(bind.recyclerViewMonth) { month ->
                updateDayList(yearAdapter.currentValue, month)
            }
            val correctPosition = abs(selectedMonth - it.centerValue) + it.centerPosition - 1
            bind.recyclerViewMonth.scrollToPosition(correctPosition)
        }
    }

    private fun onBindDayList(list: List<Int>) {
        val diff = list.size - dayAdapter.size - 1
        dayAdapter.set(list)
        bind.recyclerViewDay.scrollToPosition(dayAdapter.centerPosition + diff)
    }

}