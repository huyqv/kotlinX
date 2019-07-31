package com.huy.kotlin.ui.transaction

import android.os.Bundle
import com.huy.kotlin.R
import com.huy.kotlin.base.view.BaseActivity
import com.huy.kotlin.ui.member.ColorFragment
import kotlinx.android.synthetic.main.activity_transaction.*


/**
 * -------------------------------------------------------------------------------------------------
 *
 * @Project: Kotlin
 * @Created: Huy QV 2019/04/13
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class TransactionActivity : BaseActivity() {

    override fun layoutResource() = R.layout.activity_transaction

    override fun fragmentContainer() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBack.setOnClickListener {
            onBackPressed()
        }

        viewNext.setOnClickListener {
            val fragment = ColorFragment()
            fragment.color = RandomColor.next()
            add(fragment)
        }
    }


}
