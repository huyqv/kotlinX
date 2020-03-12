package com.huy.library.extension

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.huy.library.Library
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * -------------------------------------------------------------------------------------------------
 *
 * @Project: Kotlin
 * @Created: Huy QV 2019/04/12
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
private val app: Application get() = Library.app

val appVersion: String
    get() {
        return try {
            app.packageManager.getPackageInfo(app.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            return "v1.0"
        }
    }

val packageName: String
    get() = app.applicationContext.packageName

val statusBarHeight: Int
    get() {
        var result = 0
        val resourceId = app.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) result = app.resources.getDimensionPixelSize(resourceId)
        return result
    }

fun toast(message: String?) {
    message ?: return
    if (isOnUiThread) {
        Toast.makeText(app.applicationContext, message, Toast.LENGTH_SHORT).show()
    } else {
        uiHandler.post { Toast.makeText(app.applicationContext, message, Toast.LENGTH_SHORT).show() }
    }
}

fun toast(@StringRes res: Int?, vararg arguments: Any) {
    res ?: return
    val message = try {
        app.resources.getString(res, *arguments)
    } catch (ex: Resources.NotFoundException) {
        return
    }
    toast(message)
}

fun anim(@AnimRes res: Int): Animation {
    return AnimationUtils.loadAnimation(app, res)
}

fun drawable(@DrawableRes res: Int): Drawable? {
    return ContextCompat.getDrawable(app, res)
}

fun color(@ColorRes res: Int): Int {
    return ContextCompat.getColor(app, res)
}

fun string(@StringRes res: Int): String {
    return app.getString(res)
}

fun string(@StringRes res: Int, vararg args: Any?): String {
    return try {
        String.format(app.getString(res), *args)
    } catch (ignore: Exception) {
        ""
    }
}

fun restartApp() {

    val intent = app.packageManager.getLaunchIntentForPackage(packageName)
    intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    app.startActivity(intent)
}

fun keyHash() {

    try {
        val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            app.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES).signingInfo.signingCertificateHistory
        } else {
            @Suppress("DEPRECATION")
            app.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures
        }
        for (signature in signatures) {
            val md = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
        }

    } catch (ignore: PackageManager.NameNotFoundException) {
    } catch (ignore: NoSuchAlgorithmException) {
    }

}

fun realPathFromURI(uri: Uri): String? {
    val projection = arrayOf(MediaStore.Images.Media._ID)
    val cursor = app.contentResolver.query(uri, projection, null, null, null)
            ?: return uri.path ?: null
    try {
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    } finally {
        cursor.close()
        return null
    }
    return null
}

