package com.huy.kotlin.ui

import android.os.Bundle
import android.view.View
import com.huy.kotlin.R
import com.huy.kotlin.base.arch.ArchActivity
import com.huy.kotlin.ui.animation.AnimationFragment
import com.huy.kotlin.ui.fm.FragmentManagerActivity
import com.huy.kotlin.ui.format.TextMaskFragment
import com.huy.kotlin.ui.interval.RxIntervalFragment
import com.huy.kotlin.ui.member.*
import com.huy.kotlin.ui.recycler.diff.AsyncDiffFragment
import com.huy.kotlin.ui.recycler.loadMore.LoadMoreFragment
import com.huy.kotlin.ui.recycler.paged.PagingFragment
import com.huy.kotlin.ui.tab.TabFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/29
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class MainActivity : ArchActivity<MainVM>() {

    override val layoutResource: Int = R.layout.activity_main

    override val fragmentContainerId: Int = R.id.container

    override val viewModelClass = MainVM::class.java

    override fun onCreated(state: Bundle?) {
        addClickListener(
                itemHandlerThread, itemLoadMore, itemAsyncDiff, itemSetting,
                itemEditText, itemTextMask, itemMenu, itemAppBar, itemDialog, itemTab, itemPaging,
                itemAnimation, itemTransaction
        )
    }

    override fun onRegisterLiveData() {
    }

    override fun onViewClick(view: View?) {
        when (view) {
            itemHandlerThread -> add(RxIntervalFragment())
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