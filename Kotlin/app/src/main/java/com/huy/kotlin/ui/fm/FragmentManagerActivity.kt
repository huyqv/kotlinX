package com.huy.kotlin.ui.fm

import android.os.Bundle
import com.huy.kotlin.R
import com.huy.kotlin.base.view.BaseActivity
import com.huy.kotlin.ui.member.ColorFragment
import com.huy.library.util.Colors
import kotlinx.android.synthetic.main.activity_translate.*


/**
 * -------------------------------------------------------------------------------------------------
 *
 * @Project: Kotlin
 * @Created: Huy QV 2019/04/13
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class FragmentManagerActivity : BaseActivity() {

    override val layoutResource = R.layout.activity_translate

    override val fragmentContainer = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBack.setOnClickListener {
            onBackPressed()
        }

        viewNext.setOnClickListener {
            val fragment = ColorFragment()
            fragment.color = Colors.next()
            add(fragment)
        }
    }


}
