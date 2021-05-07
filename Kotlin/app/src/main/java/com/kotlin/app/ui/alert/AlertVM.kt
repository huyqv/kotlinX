package com.kotlin.app.ui.alert

import androidx.lifecycle.MutableLiveData
import com.kotlin.app.ui.vm.BaseVM

class AlertVM : BaseVM() {

    val arg = MutableLiveData<AlertArg>()

}