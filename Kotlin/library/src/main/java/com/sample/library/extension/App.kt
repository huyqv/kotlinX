package com.sample.library.extension

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import com.sample.library.app
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

val appVersion: String
    get() {
        return try {
            app.packageManager.getPackageInfo(app.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            return "v1.0"
        }
    }

fun isServiceRunning(serviceClass: Class<*>): Boolean {
    val manager = app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
    @Suppress("DEPRECATION")
    for (service in manager!!.getRunningServices(Int.MAX_VALUE)) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}

val packageName: String get() = app.applicationContext.packageName

val packageUrl: String get() = "package:$packageName"

val statusBarHeight: Int
    get() {
        val resources = app.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) return resources.getDimensionPixelSize(resourceId)
        return 0
    }

val gridSpanCount: Int
    get() {
        return if (isTablet) 3 else 2
    }

val navigationBarHeight: Int
    get() {
        val resources: Resources = app.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

var currentToast: Toast? = null

fun toast(message: String?) {
    message ?: return
    onUi {
        currentToast = Toast.makeText(app.applicationContext, message, Toast.LENGTH_SHORT)
        currentToast?.show()
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
            app.packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            ).signingInfo.signingCertificateHistory
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


