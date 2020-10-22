package com.example.kotlin.ui.selectable

import androidx.lifecycle.MutableLiveData
import com.example.kotlin.base.vm.BaseViewModel


/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/10/08
 * @Description:
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class SelectableVM : BaseViewModel() {

    val argLiveData = MutableLiveData<SelectableArg>()

    var arg: SelectableArg?
        get() = argLiveData.value
        set(value) {
            argLiveData.postValue(value)
        }
}