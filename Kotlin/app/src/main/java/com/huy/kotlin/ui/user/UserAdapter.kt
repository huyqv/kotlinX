package com.huy.kotlin.ui.user

import android.view.View
import com.huy.kotlin.R
import com.huy.kotlin.base.adapter.BaseRecyclerAdapter
import com.huy.kotlin.extension.loadUser
import kotlinx.android.synthetic.main.item_user_grid.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class UserAdapter : BaseRecyclerAdapter<User>() {

    override fun blankLayoutResource(): Int {
        return R.layout.item_blank
    }

    override fun footerLayoutResource(): Int {
        return R.layout.item_footer
    }

    override fun layoutResource(model: User, position: Int) = R.layout.item_user_grid

    override fun View.onBindModel(model: User, position: Int, layout: Int) {
        model.run {
            val fn = if (firstName.isNullOrEmpty()) "" else "$firstName\n"
            userTextViewName.text = "$fn$lastName"
            userImageViewAvatar.loadUser(avatar)
        }
    }

}