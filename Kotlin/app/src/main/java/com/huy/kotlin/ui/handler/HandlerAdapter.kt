package com.huy.kotlin.ui.handler

import android.view.View
import com.huy.kotlin.R
import com.huy.kotlin.ui.model.User
import com.huy.kotlin.util.loadUser
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
class HandlerAdapter : BaseRecyclerAdapter<User>() {

    override var blankLayoutResource = R.layout.item_blank

    override var footerLayoutResource = R.layout.item_footer

    override fun layoutResource(model: User, position: Int) = R.layout.item_user

    override fun View.onBindModel(model: User, position: Int, layout: Int) {
        model.run {
            val fn = if (firstName.isNullOrEmpty()) "" else "$firstName "
            userTextViewName.text = "$fn$lastName"
            userImageViewAvatar.loadUser(avatar)
        }
    }

}