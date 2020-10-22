package com.example.kotlin.ui.date

import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.kotlin.R
import com.example.kotlin.base.ext.activityViewModel
import com.example.kotlin.base.ext.setNavResult
import com.example.kotlin.base.view.BaseDialog
import kotlinx.android.synthetic.main.date.*
import kotlin.math.abs

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/10/06
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class DateFragment : BaseDialog() {

    private val vm: DateVM by lazy {
        activityViewModel(DateVM::class)
    }

    /**
     * [BaseDialog] override
     */
    override fun layoutResource(): Int {
        return R.layout.date
    }

    override fun onViewCreated() {
        addClickListener(dateViewDone)
    }

    override fun onLiveDataObserve() {
        vm.argLiveData.observe {
            vm.updateDayList(it)
        }

        vm.daysLiveData.observe {
            if (dateRecyclerViewDay.adapter == null) {
                onBindDaysInit()
            }
            onBindDayList(it)
        }
    }

    override fun onViewClick(v: View?) {
        when (v) {
            dateViewDone -> {
                setNavResult(vm.arg?.key, currentDate)
                findNavController().navigateUp()
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
            return vm.getCurrentDate(yearAdapter.currentValue, monthAdapter.currentValue, dayAdapter.currentValue)
        }

    private fun onBindDaysInit() {
        onBindYearList()
        onBindMonthList()
        dayAdapter.snap(dateRecyclerViewDay)
    }

    private fun onBindYearList() {
        yearAdapter.also {
            it.set(vm.yearList)
            it.snap(dateRecyclerViewYear) { year ->
                vm.updateDayList(year, monthAdapter.currentValue)
            }
            val correctPosition = abs(vm.selectedYear - it.centerValue) + it.centerPosition - 1
            dateRecyclerViewYear.scrollToPosition(correctPosition)
        }
    }

    private fun onBindMonthList() {
        monthAdapter.also {
            it.set(vm.monthList)
            it.snap(dateRecyclerViewMonth) { month ->
                vm.updateDayList(yearAdapter.currentValue, month)
            }
            val correctPosition = abs(vm.selectedMonth - it.centerValue) + it.centerPosition - 1
            dateRecyclerViewMonth.scrollToPosition(correctPosition)
        }
    }

    private fun onBindDayList(list: List<Int>) {
        val diff = list.size - dayAdapter.size - 1
        dayAdapter.set(list)
        dateRecyclerViewDay.scrollToPosition(dayAdapter.centerPosition + diff)
    }


}