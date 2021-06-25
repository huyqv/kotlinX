package sample.ui.main

import androidx.lifecycle.MutableLiveData
import com.kotlin.app.ui.main.MainVM
import sample.ui.alert.AlertArg
import sample.ui.date.DateArg
import sample.ui.selectable.SelectableArg
import sample.ui.web.WebArg

class DialogVM : MainVM() {

    val dateLiveData = MutableLiveData<DateArg>()

    val alertLiveData = MutableLiveData<AlertArg?>()

    val webLiveData = MutableLiveData<WebArg>()

    val selectableLiveData = MutableLiveData<SelectableArg>()

}
