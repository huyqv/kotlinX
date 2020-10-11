package com.huy.kotlin.base.view

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.huy.kotlin.R
import com.huy.library.extension.addFragment
import com.huy.library.extension.replaceFragment
import com.huy.library.view.ViewClickListener

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class BaseActivity : AppCompatActivity() {

    /**
     * [AppCompatActivity] override
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResource())
        navigationHostId()?.also { nav = findNavController(it) }
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
     * [BaseActivity] open implements
     */
    protected open fun onViewClick(v: View?) {}

    protected open fun navigationHostId(): Int? {
        return null
    }

    protected open fun fragmentContainerId(): Int {
        throw Resources.NotFoundException("BaseView.FragmentContainer() must be implement with resource id return value")
    }

    fun add(fragment: Fragment, stack: Boolean = true) {
        addFragment(fragment, fragmentContainerId(), stack)
    }

    fun replace(fragment: Fragment, stack: Boolean = true) {
        replaceFragment(fragment, fragmentContainerId(), stack)
    }

    fun <T : Fragment> remove(cls: Class<T>) {
        remove(cls)
    }

    /**
     * [BaseActivity] properties
     */
    private val onViewClick: ViewClickListener by lazy {
        object : ViewClickListener() {
            override fun onClicks(v: View?) {
                onViewClick(v)
            }
        }
    }

    var nav: NavController? = null

    fun <T> LiveData<T>.observe(block: (T) -> Unit) {
        observe(this@BaseActivity, Observer(block))
    }

    fun addClickListener(vararg views: View?) {
        views.forEach {
            it?.setOnClickListener(onViewClick)
        }
    }

}