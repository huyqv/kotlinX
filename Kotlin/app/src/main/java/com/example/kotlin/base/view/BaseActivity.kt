package com.example.kotlin.base.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.kotlin.R
import com.example.kotlin.base.dialog.ConfirmDialog
import com.example.kotlin.base.dialog.MessageDialog
import com.example.kotlin.base.dialog.ProgressDialog
import com.example.library.extension.addFragment
import com.example.library.extension.removeFragment
import com.example.library.extension.replaceFragment
import com.example.library.view.ViewClickListener

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
     * [AppCompatActivity] override
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResource())
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        super.startActivityForResult(intent, requestCode, options)
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.activity_pop_enter, R.anim.activity_pop_exit)
    }

    /**
     * [BaseActivity] abstract implements
     */
    abstract fun layoutResource(): Int

    abstract fun onViewCreated()

    /**
     * [BaseView] implement
     */
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog(this)
    }

    final override val baseActivity: BaseActivity? get() = this

    final override fun showProgress() {

    }

    final override fun hideProgress() {

    }

    final override fun alert(message: String?) {
        message ?: return
        MessageDialog(this).apply {
            message(message)
            show()
        }
    }

    final override fun alert(message: String?, block: () -> Unit) {
        message ?: return
        ConfirmDialog(this).run {
            message(message)
            onConfirm(block)
            show()
        }
    }

    final override fun add(fragment: Fragment, stack: Boolean) {
        addFragment(fragment, fragmentContainerId(), stack)
    }

    final override fun replace(fragment: Fragment, stack: Boolean) {
        replaceFragment(fragment, fragmentContainerId(), stack)
    }

    final override fun <T : Fragment> remove(cls: Class<T>) {
        removeFragment(cls)
    }

    final override fun popBackStack() {
        supportFragmentManager.popBackStack()
    }

    /**
     * [BaseActivity] properties
     */
    val nav: NavController get() = findNavController(navigationHostId())

    protected open fun navigationHostId(): Int {
        throw NullPointerException("navigationHostId no has implement")
    }

    protected open fun fragmentContainerId(): Int {
        throw NullPointerException("fragmentContainerId no has implement")
    }

    private val onViewClick: ViewClickListener by lazy {
        object : ViewClickListener() {
            override fun onClicks(v: View?) {
                onViewClick(v)
            }
        }
    }

    fun addClickListener(vararg views: View?) {
        views.forEach {
            it?.setOnClickListener(onViewClick)
        }
    }

    protected open fun onViewClick(v: View?) {}

    fun <T> LiveData<T>.observe(block: (T) -> Unit) {
        observe(this@BaseActivity, Observer(block))
    }

}