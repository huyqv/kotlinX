package com.huy.kotlin.ui.tab

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.huy.kotlin.R
import com.huy.kotlin.base.adapter.FragmentAdapter
import com.huy.kotlin.base.view.BaseFragment
import com.huy.kotlin.ui.member.ColorFragment
import com.huy.library.extension.color
import com.huy.library.view.PageTransformer
import kotlinx.android.synthetic.main.fragment_tab.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/29
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class TabFragment : BaseFragment() {

    override val layoutResource = R.layout.fragment_tab

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager.config()

        tabLayout.config()
    }

    private fun ViewPager.config() {
        adapter = FragmentAdapter(childFragmentManager,
                getFragment(R.color.colorIndianRed, PageTransformer.Slide2()),
                getFragment(R.color.colorTurquoise, PageTransformer.Slide()),
                getFragment(R.color.colorAmethyst, PageTransformer.Stack()),
                getFragment(R.color.colorSunFlower, PageTransformer.FadeZoom())
        )
    }

    private fun ViewPager.getFragment(@ColorRes color: Int, transformer: ViewPager.PageTransformer?): ColorFragment {
        return ColorFragment().apply {
            this.color = color
            this.click = { setPageTransformer(false, transformer) }
        }
    }

    private fun TabLayout.config() {
        setupWithViewPager(viewPager)
        setSelectedTabIndicatorColor(color(android.R.color.white))
        setIcon(0, R.drawable.ic_send, Color.WHITE)
        setIcon(1, R.drawable.ic_camera)
        setIcon(2, R.drawable.ic_edit)
        setIcon(3, R.drawable.ic_settings)
        addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p: TabLayout.Tab?) {
                p?.icon?.setTint(Color.LTGRAY)
            }

            override fun onTabSelected(p: TabLayout.Tab?) {
                p?.icon?.setTint(Color.WHITE)
            }
        })
    }

    private fun TabLayout.setIcon(index: Int, @DrawableRes drawable: Int, color: Int = Color.LTGRAY) {
        getTabAt(index)?.setIcon(drawable)
        getTabAt(index)?.icon?.setTint(color)
    }

}
