package com.huy.kotlin.ui

import android.os.Bundle
import android.view.View
import com.huy.kotlin.R
import com.huy.kotlin.base.arch.ArchActivity
import com.huy.kotlin.ui.animation.AnimationFragment
import com.huy.kotlin.ui.fm.FragmentManagerActivity
import com.huy.kotlin.ui.handler.HandlerFragment
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

    override val fragmentContainer: Int = R.id.container

    override val viewModelClass = MainVM::class.java

    override fun onCreated(state: Bundle?) {
        addClickListener(itemHandlerThread, itemLoadMore, itemMultiView, itemAsyncDiff, itemSetting,
                itemEditText, itemMenu, itemAppBar, itemDialog, itemTab, itemPaging, itemAnimation, itemTransaction)
    }

    override fun onRegisterLiveData() {
    }

    override fun onViewClick(view: View) {
        when (view) {
            itemHandlerThread -> add(HandlerFragment())
            itemLoadMore -> add(LoadMoreFragment())
            itemMultiView -> add(PagingFragment())
            itemAsyncDiff -> add(AsyncDiffFragment())
            itemSetting -> add(SettingsFragment())
            itemEditText -> add(EditTextsFragment())
            itemMenu -> add(MenuItemsFragment())
            itemAppBar -> add(AppBarsFragment())
            itemDialog -> add(DialogsFragment())
            itemTab -> add(TabFragment())
            itemPaging -> add(PagingFragment())
            itemAnimation -> add(AnimationFragment())
            itemTransaction -> start(FragmentManagerActivity::class.java)
        }
    }

}