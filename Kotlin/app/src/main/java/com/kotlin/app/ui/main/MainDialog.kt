package com.kotlin.app.ui.main

import com.kotlin.app.ui.base.BaseDialog
import com.kotlin.app.ui.dialog.DialogVM

abstract class MainDialog : BaseDialog(), MainView {

    override val mainActivity: MainActivity? get() = requireActivity() as? MainActivity

    override val mainVM by lazy { activityVM(MainVM::class) }

    override val dialogVM by lazy { activityVM(DialogVM::class) }

}