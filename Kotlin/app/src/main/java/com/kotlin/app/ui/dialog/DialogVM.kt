package com.kotlin.app.ui.dialog

import androidx.lifecycle.MutableLiveData
import com.kotlin.app.ui.dialog.alert.AlertArg
import com.kotlin.app.ui.dialog.date.DateArg
import com.kotlin.app.ui.dialog.selectable.SelectableArg
import com.kotlin.app.ui.dialog.web.WebArg
import com.kotlin.app.ui.main.MainVM

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2021/05/24
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class DialogVM : MainVM() {

    val dateLiveData = MutableLiveData<DateArg>()

    val alertLiveData = MutableLiveData<AlertArg?>()

    val webLiveData = MutableLiveData<WebArg>()

    val selectableLiveData = MutableLiveData<SelectableArg>()

}
