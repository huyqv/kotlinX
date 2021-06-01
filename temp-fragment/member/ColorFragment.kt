package com.kotlin.sample.ui.member

import com.kotlin.sample.R
import com.kotlin.sample.base.view.BaseFragment
import com.example.library.extension.color
import com.example.library.extension.toast
import kotlinx.android.synthetic.main.fragment_color.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/03/30
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class ColorFragment : BaseFragment() {

    var color: Int = color(R.color.colorPrimary)

    var click: (() -> Unit)? = null

    override fun layoutResource(): Int {
        return R.layout.color
    }

    override fun onViewCreated() {
        view?.setBackgroundResource(color)
        textView.setOnClickListener { click?.also { click!!() } }
    }

    override fun onLiveDataObserve() {
    }

    override fun onBackPressed(): Boolean {
        toast("onBackPressed")
        return super.onBackPressed()
    }

}