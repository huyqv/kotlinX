package sample.ui.main

import androidx.viewbinding.ViewBinding
import com.kotlin.app.ui.base.BaseDialogFragment
import com.sample.library.extension.lazyActivityVM

abstract class MainDialogFragment<B : ViewBinding> : BaseDialogFragment<B>(), MainView {

    override val mainActivity get() = requireActivity() as? MainActivity

    override val mainVM by lazyActivityVM(MainVM::class)

    override val dialogVM by lazyActivityVM(DialogVM::class)

}