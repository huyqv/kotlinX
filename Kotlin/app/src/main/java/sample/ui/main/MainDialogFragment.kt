package sample.ui.main

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.example.library.extension.activityVM
import com.kotlin.app.ui.base.BaseDialogFragment

abstract class MainDialogFragment<B : ViewBinding> : BaseDialogFragment<B>(), MainView {

    override val mainActivity get() = requireActivity() as? MainActivity

    override val mainVM by activityVM(MainVM::class)

    override val dialogVM by activityVM(DialogVM::class)

}