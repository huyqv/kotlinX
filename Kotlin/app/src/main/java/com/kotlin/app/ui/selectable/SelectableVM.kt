package com.kotlin.app.ui.selectable

import androidx.lifecycle.MutableLiveData
import com.kotlin.app.app.AppVM


/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/10/08
 * @Description:
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class SelectableVM : AppVM() {

    val argLiveData = MutableLiveData<SelectableArg>()

    var arg: SelectableArg?
        get() = argLiveData.value
        set(value) {
            argLiveData.postValue(value)
        }
}