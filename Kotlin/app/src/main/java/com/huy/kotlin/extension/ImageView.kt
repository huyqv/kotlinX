package com.huy.kotlin.extension

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.huy.kotlin.R

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */

@GlideModule
class MyGlideApp : AppGlideModule()

fun ImageView.loadUser(url: String?) {
    GlideApp.with(context).load(url)
            .placeholder(R.mipmap.img_user)
            .error(R.mipmap.img_user)
            .override(this.width, this.height)
            .into(this)
}

fun ImageView.loadImage(url: String?) {
    GlideApp.with(context).load(url)
            .placeholder(R.drawable.drw_rect_dashed)
            .error(R.drawable.drw_rect_dashed)
            .override(this.width, this.height)
            .into(this)
}

fun ImageView.load(url: String?, @DrawableRes res: Int) {
    GlideApp.with(context).load(url)
            .placeholder(res)
            .error(res)
            .override(this.width, this.height)
            .into(this)
}

fun ImageView.load(url: String?) {
    GlideApp.with(context)
            .load(url)
            .override(this.width, this.height)
            .into(this)
}

fun ImageView.load(uri: Uri?) {
    GlideApp.with(context)
            .load(uri)
            .override(this.width, this.height)
            .into(this)
}

fun ImageView.load(@DrawableRes res: Int) {
    GlideApp.with(context)
            .load(res)
            .override(this.width, this.height)
            .into(this)
}

