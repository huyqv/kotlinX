package com.huy.library.extension

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
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
import java.io.File
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

val contentResolver: ContentResolver get() = Library.app.contentResolver

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

fun safeClose(closeable: Closeable?) {
    closeable ?: return
    try {
        closeable.close()
    } catch (ignored: Exception) {
    }
}

fun getImageUri(bitmap: Bitmap): Uri? {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        @Suppress("DEPRECATION")
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis().toString())
        put(MediaStore.MediaColumns.MIME_TYPE, "image/*")
        put(MediaStore.MediaColumns.IS_PENDING, 1)
    }
    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    return null
}

fun getImageUri(file: File): Uri? {

    val filePath = file.absolutePath

    val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID),
            null,
            arrayOf(filePath), null)

    if (cursor != null && cursor.moveToFirst()) {
        val id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
        cursor.close()
        return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id)
    }

    if (file.exists()) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis().toString())
            put(MediaStore.MediaColumns.MIME_TYPE, "image/*")
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    return null

}

fun realPathFromURI(uri: Uri): String? {
    val projection = arrayOf(MediaStore.Images.Media._ID)
    val cursor = contentResolver.query(uri, projection, null, null, null)
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

