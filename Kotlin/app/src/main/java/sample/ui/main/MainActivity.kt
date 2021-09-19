package sample.ui.main

import android.view.LayoutInflater
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.kotlin.app.R
import com.kotlin.app.databinding.SampleBinding
import com.kotlin.app.ui.base.BaseActivity
import com.sample.library.extension.viewModel

class MainActivity : BaseActivity<SampleBinding>(), MainView {

    override val mainActivity: MainActivity? get() = this

    override val mainVM by viewModel(MainVM::class)

    override val dialogVM by viewModel(DialogVM::class)

    override fun activityNavController(): NavController {
        return findNavController(R.id.fragment)
    }

    override fun inflating(): (LayoutInflater) -> SampleBinding {
        return SampleBinding::inflate
    }

    override fun onViewCreated() {
    }

    override fun onLiveDataObserve() {

    }

}






