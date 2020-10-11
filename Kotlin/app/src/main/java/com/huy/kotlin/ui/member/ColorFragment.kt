package com.huy.kotlin.ui.member

import com.huy.kotlin.R
import com.huy.kotlin.base.view.BaseFragment
import com.huy.library.extension.color
import com.huy.library.extension.toast
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
        return R.layout.fragment_color
    }

    override fun onViewCreated() {
        view?.setBackgroundResource(color)
        textView.setOnClickListener { click?.also { click!!() } }
    }

    override fun onBackPressed(): Boolean {
        toast("onBackPressed")
        return super.onBackPressed()
    }

}