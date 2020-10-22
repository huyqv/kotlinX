package com.example.kotlin.ui.fm

import android.view.View
import com.example.kotlin.R
import com.example.kotlin.base.view.BaseActivity
import com.example.kotlin.ui.member.ColorFragment
import com.example.library.extension.HORIZONTAL_ANIMATIONS
import com.example.library.extension.PARALLAX_ANIMATIONS
import com.example.library.extension.VERTICAL_ANIMATIONS
import com.example.library.extension.replaceFragment
import com.example.library.util.Colors
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

    override fun layoutResource(): Int {
        return R.layout.activity_translate
    }

    override fun fragmentContainerId(): Int {
        return R.id.container
    }

    override fun onViewCreated() {
        addClickListener(viewBack, viewNextVer, viewNextHor, viewNextPar)
    }

    override fun onLiveDataObserve() {
    }

    override fun onViewClick(v: View?) {
        when (v) {
            viewBack -> onBackPressed()
            else -> {
                val fragment = ColorFragment().apply {
                    color = Colors.next()
                }
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
