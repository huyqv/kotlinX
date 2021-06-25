package sample.ui.main

import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.library.extension.viewModel
import com.kotlin.app.R
import com.kotlin.app.databinding.SampleBinding
import com.kotlin.app.ui.base.BaseActivity

class MainActivity : BaseActivity<SampleBinding>(SampleBinding::inflate), MainView {

    override val mainActivity: MainActivity? get() = this

    override val mainVM by viewModel(MainVM::class)

    override val dialogVM by viewModel(DialogVM::class)

    override fun navController(): NavController? {
        return findNavController(R.id.fragment)
    }

    override fun onViewCreated() {
    }

    override fun onLiveDataObserve() {

    }

}






