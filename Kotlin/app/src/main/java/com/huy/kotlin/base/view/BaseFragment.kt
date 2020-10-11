package com.huy.kotlin.base.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.huy.library.view.ViewClickListener

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BaseFragment : Fragment() {


    /**
     * [Fragment] override
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackCallback)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutResource(), container, false)
        view.setOnTouchListener { _, _ -> true }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
    }


    /**
     * [BaseFragment] abstract implements
     */
    abstract fun layoutResource(): Int

    abstract fun onViewCreated()


    /**
     * [BaseFragment] open implements
     */
    protected open fun onViewClick(v: View?) {}

    protected open fun onBackPressed(): Boolean {
        remove(this::class.java)
        return true
    }

    fun add(fragment: Fragment, stack: Boolean = true) {
        (activity as? BaseActivity)?.add(fragment, stack)
    }

    fun replace(fragment: Fragment, stack: Boolean = true) {
        (activity as? BaseActivity)?.replace(fragment, stack)
    }

    fun <T : Fragment> remove(cls: Class<T>) {
        (activity as? BaseActivity)?.remove(cls)
    }

    /**
     * [BaseFragment] properties
     */
    private val onViewClick: ViewClickListener by lazy {
        object : ViewClickListener() {
            override fun onClicks(v: View?) {
                onViewClick(v)
            }
        }
    }

    private val onBackCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (onBackPressed()) {
                findNavController().popBackStack()
            }
        }
    }

    fun <T> LiveData<T>.observe(block: (T) -> Unit) {
        observe(viewLifecycleOwner, Observer(block))
    }

    fun addClickListener(vararg views: View?) {
        views.forEach {
            it?.setOnClickListener(onViewClick)
        }
    }

}