package com.huy.kotlin.base.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.huy.kotlin.base.dialog.ProgressDialog
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BaseFragment : Fragment(), BaseView {


    /**
     * [BaseView] implement
     */
    override val baseActivity: BaseActivity? get() = activity as? BaseActivity

    override val fragmentActivity: FragmentActivity? get() = requireActivity()

    override var onViewClick: View.OnClickListener? = null

    override var progressDialog: ProgressDialog? = null
        get() = baseActivity?.getProgress()

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (onBackPressed()) {
                this.remove()
                activity?.supportFragmentManager?.popBackStack()
            }
        }
    }

    /**
     * [Fragment] override
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutResource, container, false)
        view.setOnTouchListener { _, _ -> true }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onViewClick = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            onReceivedDataResult(requestCode, data)
        }
    }

    override fun onViewClick(view: View) {
    }


    /**
     * [BaseFragment] utilities
     */
    open fun onReceivedDataResult(code: Int, intent: Intent) {
    }

    open fun onGrantedPermission(permission: String, block: () -> Unit) {
        RxPermissions(this)
                .request(permission)
                .subscribe { granted -> if (granted) block() }
    }

    open fun onBackPressed(): Boolean {
        return true
    }


    /**
     * Observer
     */
    fun <T> LiveData<T?>.observe(block: (T?) -> Unit) {
        observe(viewLifecycleOwner, Observer { block(it) })
    }

    fun <T> LiveData<T?>.nonNull(block: (T) -> Unit) {
        observe(viewLifecycleOwner, Observer { if (null != it) block(it) })
    }


}