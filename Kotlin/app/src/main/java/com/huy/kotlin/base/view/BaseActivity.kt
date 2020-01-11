package com.huy.kotlin.base.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.huy.kotlin.R
import com.huy.kotlin.app.App
import com.huy.kotlin.base.dialog.ProgressDialog
import com.huy.library.view.PreventClickListener
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


    /**
     * [BaseView] implement
     */
    override val baseActivity: BaseActivity? get() = this

    override val fragmentActivity: FragmentActivity? get() = this

    override val fragmentContainer: Int? = null

    override val onViewClick: PreventClickListener by lazy {
        object : PreventClickListener() {
            override fun onViewClick(v: View?) {
                onViewClick(v)
            }
        }
    }

    override var progressDialog: ProgressDialog? = null


    /**
     * [AppCompatActivity] override
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResource)
        progressDialog = ProgressDialog(this)
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

    override fun onViewClick(view: View) {
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


    /**
     * Observer
     */
    fun <T> LiveData<T?>.observe(block: (T?) -> Unit) {
        observe(this@BaseActivity, Observer { block(it) })
    }

    fun <T> LiveData<T?>.nonNull(block: (T) -> Unit) {
        observe(this@BaseActivity, Observer { if (null != it) block(it) })
    }

}