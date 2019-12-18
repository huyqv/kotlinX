package com.huy.library.extension

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
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
import java.io.ByteArrayOutputStream
import java.io.Closeable
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
val resources: Resources get() = Library.app.applicationContext.resources

val appVersion: String
    get() {
        return try {
            Library.app.packageManager.getPackageInfo(Library.app.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            return "v1.0"
        }
    }

val packageName: String
    get() = Library.app.applicationContext.packageName

val statusBarHeight: Int
    get() {
        var result = 0
        val resourceId = Library.app.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) result = Library.app.resources.getDimensionPixelSize(resourceId)
        return result
    }

fun toast(message: String?) {
    message ?: return
    if (isOnUiThread) {
        Toast.makeText(Library.app.applicationContext, message, Toast.LENGTH_SHORT).show()
    } else {
        UI_HANDLER.post { Toast.makeText(Library.app.applicationContext, message, Toast.LENGTH_SHORT).show() }
    }
}

fun toast(@StringRes res: Int?, vararg arguments: Any) {
    res ?: return
    val message = try {
        resources.getString(res, *arguments)
    } catch (ex: Resources.NotFoundException) {
        return
    }
    toast(message)
}

fun anim(@AnimRes res: Int): Animation {
    return AnimationUtils.loadAnimation(Library.app, res)
}

fun drawable(@DrawableRes res: Int): Drawable? {
    return ContextCompat.getDrawable(Library.app, res)
}

fun color(@ColorRes res: Int): Int {
    return ContextCompat.getColor(Library.app, res)
}

fun string(@StringRes res: Int): String {
    return Library.app.getString(res)
}

fun string(@StringRes res: Int, vararg args: Any?): String {
    return try {
        String.format(Library.app.getString(res), *args)
    } catch (ignore: Exception) {
        ""
    }
}

fun restartApp() {

    val intent = Library.app.packageManager.getLaunchIntentForPackage(packageName)
    intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    Library.app.startActivity(intent)
}

fun keyHash() {

    try {
        val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Library.app.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES).signingInfo.signingCertificateHistory
        } else {
            @Suppress("DEPRECATION")
            Library.app.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures
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

fun getImageUri(inImage: Bitmap): Uri {
    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(Library.app.contentResolver, inImage, "Title", null)
    return Uri.parse(path)
}

fun realPathFromURI(contentUri: Uri): String {

    var cursor: Cursor? = null
    try {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        cursor = Library.app.contentResolver.query(contentUri, proj, null, null, null)
        val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    } finally {
        cursor!!.close()
    }
}

fun safeClose(closeable: Closeable?) {
    closeable ?: return
    try {
        closeable.close()
    } catch (ignored: Exception) {
    }
}