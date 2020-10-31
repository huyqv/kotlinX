package com.kotlin.app.ui.alert

import androidx.lifecycle.MutableLiveData
import com.kotlin.app.app.AppVM

class AlertVM : AppVM() {

    val arg = MutableLiveData<AlertArg>()

}