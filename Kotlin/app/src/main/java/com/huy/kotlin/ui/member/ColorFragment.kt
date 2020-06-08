package com.huy.kotlin.ui.member

import android.os.Bundle
import android.view.View
import com.huy.kotlin.R
import com.huy.kotlin.base.view.BaseFragment
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

    var color: Int = 0

    var click: (() -> Unit)? = null

    override val layoutResource: Int = R.layout.fragment_color

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundResource(color)
        textView.setOnClickListener { click?.also { click!!() } }
    }

    override fun onBackPressed(): Boolean {
        toast("onBackPressed")
        return super.onBackPressed()
    }

}