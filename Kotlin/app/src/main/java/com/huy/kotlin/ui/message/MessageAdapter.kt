package com.huy.kotlin.ui.message

import android.view.View
import com.huy.kotlin.R
import com.huy.kotlin.base.adapter.BasePagedListAdapter
import com.huy.kotlin.extension.load
import com.huy.kotlin.extension.loadUser
import com.huy.library.extension.notNullOrEmpty
import com.huy.library.util.time.TimeUtil.getDateTimeAgo
import kotlinx.android.synthetic.main.item_image_left.view.*
import kotlinx.android.synthetic.main.item_image_right.view.*
import kotlinx.android.synthetic.main.item_sticker_left.view.*
import kotlinx.android.synthetic.main.item_sticker_right.view.*
import kotlinx.android.synthetic.main.item_text_left.view.*
import kotlinx.android.synthetic.main.item_text_right.view.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class MessageAdapter(var myId: Int = 0) : BasePagedListAdapter<Message>(Message.DIFF_CALLBACK) {

    override fun blankLayoutResource(): Int {
        return R.layout.item_blank
    }

    override fun footerLayoutResource(): Int {
        return R.layout.item_footer
    }

    override fun layoutResource(model: Message, position: Int): Int {

        if (model.text.notNullOrEmpty())
            return if (model.id != myId) R.layout.item_text_left else R.layout.item_text_right

        if (model.image.notNullOrEmpty())
            return if (model.id != myId) R.layout.item_image_left else R.layout.item_image_right

        if (model.sticker.notNullOrEmpty())
            return if (model.id != myId) R.layout.item_sticker_left else R.layout.item_sticker_right

        return R.layout.view_gone
    }

    override fun View.onBindModel(model: Message, position: Int, layout: Int) {
        val timeText = getDateTimeAgo(model.time)
        when (layout) {
            R.layout.item_text_left -> {
                textLeftImageViewAvatar.loadUser(model.avatar)
                textLeftTextViewContent.text = model.text
                textLeftTextViewTime.text = timeText
            }
            R.layout.item_text_right -> {
                textRightTextViewContent.text = model.text
                textRightTextViewTime.text = timeText
            }
            R.layout.item_sticker_left -> {
                stickerLeftImageViewAvatar.loadUser(model.avatar)
                stickerLeftImageViewSticker.load(model.sticker)
                stickerLeftTextViewTime.text = timeText
            }
            R.layout.item_sticker_right -> {
                stickerRightImageViewSticker.load(model.sticker)
                stickerRightTextViewTime.text = timeText
            }
            R.layout.item_image_left -> {
                imageLeftImageViewAvatar.loadUser(model.avatar)
                imageLeftImageViewContent.load(model.image)
                imageLeftTextViewTime.text = timeText
            }
            R.layout.item_image_right -> {
                imageRightImageViewContent.load(model.image)
                imageRightTextViewTime.text = timeText
            }
        }
    }

}