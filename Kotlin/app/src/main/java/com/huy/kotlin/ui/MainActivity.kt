package com.huy.kotlin.ui

import android.os.Bundle
import android.view.View
import com.huy.kotlin.R
import com.huy.kotlin.base.arch.ArchActivity
import com.huy.kotlin.ui.animation.AnimationFragment
import com.huy.kotlin.ui.generator.GeneratorFragment
import com.huy.kotlin.ui.image.ImageFragment
import com.huy.kotlin.ui.member.*
import com.huy.kotlin.ui.message.MessageFragment
import com.huy.kotlin.ui.paging.PagingFragment
import com.huy.kotlin.ui.tab.TabFragment
import com.huy.kotlin.ui.transaction.TranslateActivity
import com.huy.kotlin.ui.user.UserFragment
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

    override val layoutResource = R.layout.activity_main

    override val fragmentContainer = R.id.container

    override val viewModelClass = MainVM::class.java

    override fun onCreated(state: Bundle?) {
        addClickListener(itemHandlerThread, itemLoadMore, itemMultiView, itemImages, itemSetting,
                itemEditText, itemMenu, itemAppBar, itemDialog, itemTab, itemPaging, itemAnimation, itemTransaction)
    }

    override fun onRegisterLiveData() {
    }

    override fun onViewClick(view: View) {
        when (view) {
            itemHandlerThread -> add(GeneratorFragment())
            itemLoadMore -> add(UserFragment())
            itemMultiView -> add(MessageFragment())
            itemImages -> add(ImageFragment())
            itemSetting -> add(SettingsFragment())
            itemEditText -> add(EditTextsFragment())
            itemMenu -> add(MenuItemsFragment())
            itemAppBar -> add(AppBarsFragment())
            itemDialog -> add(DialogsFragment())
            itemTab -> add(TabFragment())
            itemPaging -> add(PagingFragment())
            itemAnimation -> add(AnimationFragment())
            itemTransaction -> start(TranslateActivity::class.java)
        }
    }

}