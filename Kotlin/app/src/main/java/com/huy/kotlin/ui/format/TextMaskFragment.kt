package com.huy.kotlin.ui.format

import android.os.Bundle
import android.view.View
import com.huy.kotlin.R
import com.huy.kotlin.base.view.BaseFragment
import com.huy.library.extension.addCashWatcher
import kotlinx.android.synthetic.main.fragment_text_mask.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2020/02/21
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class TextMaskFragment : BaseFragment() {

    override val layoutResource: Int = R.layout.fragment_text_mask

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText1.addCashWatcher()
        editText1.setText("1234567890")

        editText2.addCashWatcher(20, "USD ")
        editText2.setText("1234567890")

    }
}
