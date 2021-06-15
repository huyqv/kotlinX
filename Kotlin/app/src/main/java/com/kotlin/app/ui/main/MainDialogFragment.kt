package com.kotlin.app.ui.main

import com.kotlin.app.ui.dialog.DialogVM
import template.ui.BaseDialogFragment

abstract class MainDialogFragment : BaseDialogFragment(), MainView {

    override val mainActivity: MainActivity? get() = requireActivity() as? MainActivity

    override val mainVM by lazy { activityVM(MainVM::class) }

    override val dialogVM by lazy { activityVM(DialogVM::class) }

}