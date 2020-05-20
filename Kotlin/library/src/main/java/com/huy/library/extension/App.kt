package com.huy.library.extension

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
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

val packageName: String get() = app.applicationContext.packageName

val statusBarHeight: Int
    get() {
        var result = 0
        val resourceId = app.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) result = app.resources.getDimensionPixelSize(resourceId)
        return result
    }

val navigationBar: Int
    get() {
        val resources: Resources = app.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
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
            ?: return uri.path
    cursor.use { cursor ->
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }
}

