package com.example.kotlin.ui

import android.os.Bundle
import android.view.View
import com.example.kotlin.R
import com.example.kotlin.base.arch.ArchActivity
import com.example.kotlin.ui.animation.AnimationFragment
import com.example.kotlin.ui.diff.AsyncDiffFragment
import com.example.kotlin.ui.fm.FragmentManagerActivity
import com.example.kotlin.ui.format.TextMaskFragment
import com.example.kotlin.ui.interval.RxIntervalFragment
import com.example.kotlin.ui.loadmore.LoadMoreFragment
import com.example.kotlin.ui.member.*
import com.example.kotlin.ui.paged.PagingFragment
import com.example.kotlin.ui.tab.TabFragment
import com.example.library.extension.hideStatusBar
import com.example.library.extension.start
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.reflect.KClass

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/29
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class MainActivity : ArchActivity<MainVM>() {

    override fun localViewModelClass(): KClass<MainVM> {
        return MainVM::class
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        hideStatusBar()
        super.onCreate(savedInstanceState)
    }

    override fun layoutResource(): Int {
        return R.layout.activity_main
    }

    override fun fragmentContainerId(): Int {
        return R.id.container
    }


    override fun onViewCreated() {
    }

    override fun onCreated(state: Bundle?) {

        addClickListener(
                itemRxInterval, itemLoadMore, itemAsyncDiff, itemSetting,
                itemEditText, itemTextMask, itemMenu, itemAppBar, itemDialog, itemTab, itemPaging,
                itemAnimation, itemTransaction
        )
    }

    override fun onLiveDataObserve() {
    }

    override fun onViewClick(v: View?) {
        when (v) {
            itemRxInterval -> add(RxIntervalFragment())
            itemLoadMore -> add(LoadMoreFragment())
            itemPaging -> add(PagingFragment())
            itemAsyncDiff -> add(AsyncDiffFragment())
            itemSetting -> add(SettingsFragment())
            itemEditText -> add(EditTextsFragment())
            itemTextMask -> add(TextMaskFragment())
            itemMenu -> add(MenuItemsFragment())
            itemAppBar -> add(AppBarsFragment())
            itemDialog -> add(DialogsFragment())
            itemTab -> add(TabFragment())
            itemAnimation -> add(AnimationFragment())
            itemTransaction -> start(FragmentManagerActivity::class.java)
        }
    }

}