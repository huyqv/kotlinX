package com.huy.kotlin.ui.fm

import android.os.Bundle
import android.view.View
import com.huy.kotlin.R
import com.huy.kotlin.base.view.BaseActivity
import com.huy.kotlin.ui.member.ColorFragment
import com.huy.library.extension.HORIZONTAL_ANIMATIONS
import com.huy.library.extension.PARALLAX_ANIMATIONS
import com.huy.library.extension.VERTICAL_ANIMATIONS
import com.huy.library.extension.replaceFragment
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

    override val layoutResource: Int = R.layout.activity_translate

    override val fragmentContainerId: Int = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addClickListener(viewBack, viewNextVer, viewNextHor, viewNextPar)
    }

    override fun onViewClick(v: View?) {
        when (v) {
            viewBack -> onBackPressed()
            else -> {
                val fragment = ColorFragment()
                fragment.color = Colors.next()
                val anim = when (v) {
                    viewNextVer -> VERTICAL_ANIMATIONS
                    viewNextHor -> HORIZONTAL_ANIMATIONS
                    viewNextPar -> PARALLAX_ANIMATIONS
                    else -> null
                }
                replaceFragment(fragment, R.id.container, true, anim)
            }
        }
    }

}
