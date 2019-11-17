package com.huy.kotlin.base.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.huy.kotlin.R
import com.huy.kotlin.app.App
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
abstract class BaseActivity : AppCompatActivity(), BaseView {

    override var lastClickTime: Long = 0

    override var progressDialog: ProgressDialog? = null

    override var onViewClick: View.OnClickListener? = null

    @LayoutRes
    protected abstract fun layoutResource(): Int


    /**
     * [BaseView] implement
     */
    override fun fragmentContainer(): Int? {
        return null
    }

    override fun fragmentActivity(): FragmentActivity? {
        return this
    }

    override fun onViewClick(view: View) {
    }

    override fun popBackStack() {
        super.onBackPressed()
    }


    /**
     * [AppCompatActivity] override
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResource())
        App.instance.isForeground = true
    }

    override fun onDestroy() {
        super.onDestroy()
        progressDialog?.dismiss()
        App.instance.isForeground = false
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        super.startActivityForResult(intent, requestCode, options)
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            onReceivedDataResult(requestCode, data)
        }
    }

    override fun onBackPressed() {
        val size = supportFragmentManager.fragments.size
        if (supportFragmentManager.backStackEntryCount > 0) {
            for (i in size - 1 downTo 0) {
                val fragment = supportFragmentManager.fragments[i]
                if (fragment is BaseView) {
                    fragment.popBackStack()
                    return
                }
            }
            popBackStack()
            return
        }
        popBackStack()
    }


    /**
     * [BaseActivity] utilities
     */
    open fun onReceivedDataResult(requestCode: Int, data: Intent) {
    }

    open fun onGrantedPermission(permission: String, block: () -> Unit) {
        RxPermissions(this)
                .request(permission)
                .subscribe { granted -> if (granted) block() }
    }

}