package com.sample.library.extension

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.res.Configuration
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.sample.library.app
import java.util.*
import java.util.concurrent.TimeUnit

val androidId: String
    get() {
        return Settings.Secure.getString(app.contentResolver, Settings.Secure.ANDROID_ID)
    }

val osVersion: String
    get() = Build.VERSION.RELEASE

val osVersionCode: Int
    get() = Build.VERSION.SDK_INT

val deviceModel: String
    get() {
        return if (Build.MODEL.startsWith(Build.MANUFACTURER)) {
            Build.MODEL.capitalize()
        } else {
            Build.MODEL
        }
    }

val deviceName: String
    get() {
        return if (Build.MODEL.startsWith(Build.MANUFACTURER)) {
            Build.MODEL.capitalize()
        } else {
            Build.MANUFACTURER.capitalize() + " " + Build.MODEL
        }
    }

val timeZone: String
    get() {
        val cal = GregorianCalendar()
        val timeZone = cal.timeZone
        val mGMTOffset = timeZone.rawOffset
        return "GMT+" + TimeUnit.HOURS.convert(mGMTOffset.toLong(), TimeUnit.MILLISECONDS)
    }

val isTablet: Boolean
    get() {
        //return com.google.android.gms.common.util.DeviceProperties.isTablet(app.resources)
        return app.resources.configuration.screenLayout and
                Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

val chipSet: String
    get() {
        return try {
            @SuppressLint("PrivateApi")
            val aClass = Class.forName("android.os.SystemProperties")
            val method = aClass.getMethod("get", String::class.java)
            val platform = method.invoke(null, "ro.board.platform")

            platform as? String ?: "<$platform>"
        } catch (e: Exception) {
            "<$e>"
        }
    }

val screenWidth: Int
    get() {
        val windowManager = app.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val bounds = windowManager.currentWindowMetrics.bounds
            return bounds.width()
        }
        val dm = DisplayMetrics()
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

val screenHeight: Int
    get() {
        val windowManager = app.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val bounds = windowManager.currentWindowMetrics.bounds
            return bounds.width()
        }
        val dm = DisplayMetrics()
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

val freeMemory: Long
    get() {
        val memoryInfo = ActivityManager.MemoryInfo()
        val manager = app.getSystemService(AppCompatActivity.ACTIVITY_SERVICE) as ActivityManager
        manager.getMemoryInfo(memoryInfo)
        return (memoryInfo.availMem) / (1024 * 1024)
    }

val dpi: Int
    get() {
        return app.resources.displayMetrics.density.toInt();
    }

/**
 * [CameraCharacteristics.LENS_FACING_FRONT]
 * [CameraCharacteristics.LENS_FACING_BACK]
 * [CameraCharacteristics.LENS_FACING_EXTERNAL]
 */
fun cameraId(facing: Int): Int {
    val manager = app.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    return manager.cameraIdList.first {
        manager.getCameraCharacteristics(it)
            .get(CameraCharacteristics.LENS_FACING) == facing
    }.toInt()
}

