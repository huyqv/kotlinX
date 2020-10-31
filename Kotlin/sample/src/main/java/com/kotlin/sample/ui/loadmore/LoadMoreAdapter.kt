package com.example.kotlin.ui.loadmore

import android.view.View
import com.example.kotlin.R
import com.example.kotlin.ui.model.User
import com.example.kotlin.util.load
import com.example.library.adapter.recycler.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.loadmore_item.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class LoadMoreAdapter : BaseRecyclerAdapter<User>() {

    override var blankLayoutResource: Int = R.layout.sample_adapter_item_blank

    override var footerLayoutResource: Int = R.layout.sample_adapter_item_footer

    override fun layoutResource(model: User, position: Int) = R.layout.loadmore_item

    override fun View.onBindModel(model: User, position: Int, layout: Int) {
        userTextViewName.text = "${model.firstName ?: ""} ${model.lastName}".trim()
        userImageViewAvatar.load(model.avatar) {
            placeholder(R.mipmap.img_user)
            error(R.mipmap.img_user)
        }
    }

}