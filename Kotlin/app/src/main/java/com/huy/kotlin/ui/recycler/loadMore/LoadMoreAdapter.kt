package com.huy.kotlin.ui.recycler.loadMore

import android.view.View
import com.huy.kotlin.R
import com.huy.kotlin.ui.model.User
import com.huy.kotlin.util.load
import com.huy.library.adapter.recycler.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.item_user_grid.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class LoadMoreAdapter : BaseRecyclerAdapter<User>() {

    override var blankLayoutResource: Int = R.layout.item_blank

    override var footerLayoutResource: Int = R.layout.item_footer

    override fun layoutResource(model: User, position: Int) = R.layout.item_user_grid

    override fun View.onBindModel(model: User, position: Int, layout: Int) {
        userTextViewName.text = "${model.firstName ?: ""} ${model.lastName}".trim()
        userImageViewAvatar.load(model.avatar) {
            placeholder(R.mipmap.img_user)
            error(R.mipmap.img_user)
        }
    }

}