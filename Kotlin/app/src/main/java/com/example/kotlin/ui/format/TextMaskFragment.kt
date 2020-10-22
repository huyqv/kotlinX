package com.example.kotlin.ui.format

import com.example.kotlin.R
import com.example.kotlin.base.view.BaseFragment
import com.example.library.extension.addCashWatcher
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

    override fun layoutResource(): Int {
        return R.layout.fragment_text_mask
    }

    override fun onViewCreated() {
        editText1.addCashWatcher()
        editText1.setText("1234567890")
        editText2.addCashWatcher(20, "USD ")
        editText2.setText("1234567890")
    }

    override fun onLiveDataObserve() {
    }

}
