package com.kotlin.app.ui.main

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.example.library.extension.activityVM
import com.kotlin.app.ui.base.BaseDialogFragment

abstract class MainDialogFragment<B : ViewBinding>(block: (LayoutInflater) -> B) :
        BaseDialogFragment<B>(block), MainView {

    override val mainActivity get() = requireActivity() as? MainActivity

    override val mainVM by activityVM(MainVM::class)

}