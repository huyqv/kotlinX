package com.huy.kotlin.ui.image_owner

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.annotations.Expose
import com.huy.kotlin.R
import com.huy.kotlin.extension.GlideApp

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/01/18
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
abstract class WrapImage {

    @Expose
    var wrapDrawable: Drawable? = null

    open val wrapUrl: String? = null

    fun bind(view: ImageView?) {
        view ?: return
        if (null != wrapDrawable) {
            GlideApp.with(view.context).load(wrapDrawable)
        } else {
            GlideApp.with(view.context)
                    .load(wrapUrl)
                    .placeholder(R.drawable.drw_rect_dashed).error(R.drawable.drw_rect_dashed)
                    //..override(view.measuredWidth, view.measuredHeight).load(wrapUrl)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            if (null != resource) wrapDrawable = resource
                            return false
                        }
                    })
                    .into(view)
        }
    }
}