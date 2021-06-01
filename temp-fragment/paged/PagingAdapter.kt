package com.kotlin.sample.ui.paged

import android.view.View
import com.kotlin.sample.R
import com.kotlin.sample.ui.model.Message
import com.kotlin.sample.util.load
import com.example.library.adapter.recycler.BasePagedListAdapter
import com.example.library.extension.getDateTimeAgo
import com.example.library.extension.notNullOrEmpty
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
class PagingAdapter(var myId: Int = 0) : BasePagedzListAdapter<Message>(Message.DIFF_CALLBACK) {

    override var blankLayoutResource: Int = R.layout.sample_adapter_item_blank

    override var footerLayoutResource: Int = R.layout.sample_adapter_item_footer

    override fun layoutResource(model: Message, position: Int): Int {

        if (model.text.notNullOrEmpty())
            return if (model.id != myId) R.layout.item_text_left else R.layout.item_text_right

        if (model.image.notNullOrEmpty())
            return if (model.id != myId) R.layout.item_image_left else R.layout.item_image_right

        if (model.sticker.notNullOrEmpty())
            return if (model.id != myId) R.layout.item_sticker_left else R.layout.item_sticker_right
        if (model.sticker.notNullOrEmpty())
            return if (model.id != myId) R.layout.item_sticker_left else R.layout.item_sticker_right
        return 0
    }

    override fun View.onBindModel(model: Message, position: Int, layout: Int) {
        val timeText = getDateTimeAgo(model.time)
        when (layout) {
            R.layout.item_text_left -> {
                textLeftTextViewContent.text = model.text
                textLeftTextViewTime.text = timeText
                textLeftImageViewAvatar.load(model.avatar) {
                    placeholder(R.mipmap.img_user)
                    error(R.mipmap.img_user)
                }
            }

            R.layout.item_text_right -> {
                textRightTextViewContent.text = model.text
                textRightTextViewTime.text = timeText
            }

            R.layout.item_sticker_left -> {
                stickerLeftImageViewSticker.load(model.sticker)
                stickerLeftTextViewTime.text = timeText
                stickerLeftImageViewAvatar.load(model.avatar) {
                    placeholder(R.mipmap.img_user)
                    error(R.mipmap.img_user)
                }
            }

            R.layout.item_sticker_right -> {
                stickerRightImageViewSticker.load(model.sticker)
                stickerRightTextViewTime.text = timeText
            }

            R.layout.item_image_left -> {
                imageLeftTextViewTime.text = timeText
                imageLeftImageViewContent.load(model.image)
                imageLeftImageViewAvatar.load(model.avatar) {
                    placeholder(R.mipmap.img_user)
                    error(R.mipmap.img_user)
                }
            }

            R.layout.item_image_right -> {
                imageRightTextViewTime.text = timeText
                imageRightImageViewContent.load(model.image)
            }
        }
    }

}