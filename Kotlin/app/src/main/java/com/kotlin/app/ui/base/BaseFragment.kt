package com.kotlin.app.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.library.extension.backgroundColor
import com.example.library.extension.statusBarColor

abstract class BaseFragment<B : ViewBinding>(block: (LayoutInflater) -> B) : Fragment(), FragmentView {

    protected val bind: B by viewBinding(block)

    /**
     * [Fragment] implements
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        onBackPressed()
                    }
                })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = bind.root
        view.setOnTouchListener { _, _ -> true }
        statusBarColor(view.backgroundColor)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
        onLiveDataObserve()
    }

    /**
     * [FragmentView] implements
     */
    override val fragment: Fragment get() = this

    /**
     * [BaseFragment] properties
     */
    protected open fun onBackPressed() {
        if (!findNavController().popBackStack()) {
            requireActivity().finish()
        }
    }

}