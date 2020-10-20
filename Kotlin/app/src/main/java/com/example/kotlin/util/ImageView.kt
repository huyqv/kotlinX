package com.example.kotlin.util

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestListener

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

interface SimpleRequestListener : RequestListener<Drawable> {

    fun onCompleted()

    override fun onLoadFailed(e: GlideException?, model: Any?,
                              target: com.bumptech.glide.request.target.Target<Drawable>?,
                              isFirstResource: Boolean): Boolean {
        onCompleted()
        return true
    }

    override fun onResourceReady(resource: Drawable?, model: Any?,
                                 target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: DataSource?,
                                 isFirstResource: Boolean): Boolean {
        onCompleted()
        return true
    }
}

fun ImageView.load(url: String?, block: GlideRequest<*>.() -> Unit = {}) {
    val request = GlideApp
            .with(context)
            .load(url)
    request.block()
    request.into(this)
}

fun ImageView.load(bitmap: Bitmap?, block: GlideRequest<*>.() -> Unit = {}) {
    val request = GlideApp
            .with(context)
            .load(bitmap)
    request.block()
    request.into(this)
}

fun ImageView.load(res: Int, block: GlideRequest<*>.() -> Unit = {}) {
    val request = GlideApp
            .with(context)
            .load(res)
    request.block()
    request.into(this)
}

fun ImageView.load(bytes: ByteArray?, block: GlideRequest<*>.() -> Unit = {}) {
    val request = GlideApp
            .with(context)
            .load(bytes)
    request.block()
    request.into(this)
}

fun ImageView.load(drawable: Drawable?, block: GlideRequest<*>.() -> Unit = {}) {
    val request = GlideApp
            .with(context)
            .load(drawable)
    request.block()
    request.into(this)
}
