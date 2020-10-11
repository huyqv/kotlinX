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
import com.huy.library.extension.fullScreen
import com.huy.library.extension.start
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


    override val viewModelClass = MainVM::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        fullScreen()
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
                itemHandlerThread, itemLoadMore, itemAsyncDiff, itemSetting,
                itemEditText, itemTextMask, itemMenu, itemAppBar, itemDialog, itemTab, itemPaging,
                itemAnimation, itemTransaction
        )
    }

    override fun onRegisterLiveData() {
    }

    override fun onViewClick(v: View?) {
        when (v) {
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