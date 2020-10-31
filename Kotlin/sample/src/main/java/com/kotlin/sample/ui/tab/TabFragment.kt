package com.example.kotlin.ui.tab

import android.graphics.Color
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.viewpager2.widget.ViewPager2
import com.example.kotlin.R
import com.example.kotlin.base.view.BaseFragment
import com.example.kotlin.ui.member.ColorFragment
import com.example.library.adapter.fragment.FragmentAdapter
import com.example.library.adapter.fragment.PageTransformer
import com.example.library.extension.color
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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

    /**
     * [BaseFragment] override
     */
    override fun layoutResource(): Int {
        return R.layout.sample_tab
    }

    override fun onViewCreated() {
        configViewPager()
        configTabLayout()
    }

    override fun onLiveDataObserve() {
    }

    /**
     * [TabFragment] properties
     */
    private fun configTabLayout() {

        TabLayoutMediator(tabLayout, viewPager) { _, _ ->
        }.attach()

        tabLayout.apply {
            setSelectedTabIndicatorColor(color(android.R.color.white))
            setIcon(0, R.drawable.ic_adb, Color.WHITE)
            setIcon(1, R.drawable.ic_adb)
            setIcon(2, R.drawable.ic_adb)
            setIcon(3, R.drawable.ic_adb)
            setIcon(4, R.drawable.ic_adb)
            setIcon(5, R.drawable.ic_adb)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab?.icon?.setTint(Color.LTGRAY)
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.icon?.setTint(Color.WHITE)
                }
            })
        }
    }

    private fun configViewPager() {
        viewPager.setPageTransformer(PageTransformer.Parallax {
            R.id.parallaxContent
        })
        viewPager.adapter = FragmentAdapter(childFragmentManager, viewLifecycleOwner).apply {
            addFragments(
                    getFragment(R.color.colorIndianRed, PageTransformer.HorizontalSlide()),
                    getFragment(R.color.colorTurquoise, PageTransformer.ReservedVerticalSlide()),
                    getFragment(R.color.colorAmethyst, PageTransformer.Stack()),
                    getFragment(R.color.colorSunFlower, PageTransformer.FadeZoom()),
                    getFragment(R.color.colorPeterRiver, PageTransformer.Fade()),
                    getFragment(R.color.colorGrey, PageTransformer.None())
            )
        }
    }

    private fun getFragment(@ColorRes color: Int, transformer: ViewPager2.PageTransformer?): ColorFragment {
        return ColorFragment().apply {
            this.color = color
            this.click = { viewPager.setPageTransformer(transformer) }
        }
    }

    private fun setIcon(index: Int, @DrawableRes drawable: Int, color: Int = Color.LTGRAY) {
        tabLayout.getTabAt(index)?.setIcon(drawable)
        tabLayout.getTabAt(index)?.icon?.setTint(color)
    }

}
