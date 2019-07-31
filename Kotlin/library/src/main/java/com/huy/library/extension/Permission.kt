package com.huy.library.extension

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/30
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun Context.isGranted(@RequiresPermission permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.grantedNetworkPermission(): Boolean {

    if (!isGranted(Manifest.permission.ACCESS_NETWORK_STATE)) return false
    if (!isGranted(Manifest.permission.ACCESS_WIFI_STATE)) return false
    return true
}

fun Activity.request(requestCode: Int, vararg permission: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions(permission, requestCode)
    } else {
        ActivityCompat.requestPermissions(this, permission, requestCode)
    }

}

fun Fragment.request(requestCode: Int, vararg permission: String) {
    requestPermissions(permission, requestCode)
}


const val STORAGE_REQUEST_CODE = 9004530

fun Activity.requestWriteExternalStorage(requestCode: Int = STORAGE_REQUEST_CODE) {
    request(requestCode, Manifest.permission.WRITE_EXTERNAL_STORAGE)
}

fun Activity.requestReadExternalStorage(requestCode: Int = STORAGE_REQUEST_CODE) {
    request(requestCode, Manifest.permission.READ_EXTERNAL_STORAGE)
}

fun Fragment.requestWriteExternalStorage(requestCode: Int = STORAGE_REQUEST_CODE) {
    request(requestCode, Manifest.permission.WRITE_EXTERNAL_STORAGE)
}

fun Fragment.requestReadExternalStorage(requestCode: Int = STORAGE_REQUEST_CODE) {
    request(requestCode, Manifest.permission.READ_EXTERNAL_STORAGE)
}


const val LOCATION_REQUEST_CODE = 9004531
val LOCATION_PERMISSION: Array<String> by lazy {
    arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    )
}

fun Context.isGrantedLocation(): Boolean {
    return isGranted(Manifest.permission.ACCESS_FINE_LOCATION) && isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
}

fun Fragment.isGrantedLocation(): Boolean {
    return context?.isGranted(Manifest.permission.ACCESS_FINE_LOCATION) == true &&
            context?.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION) == true
}

fun Fragment.requestLocation(requestCode: Int = LOCATION_REQUEST_CODE) {
    request(
            requestCode,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    )
}

fun Activity.requestLocation(requestCode: Int = LOCATION_REQUEST_CODE) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions(LOCATION_PERMISSION, requestCode)
    } else {
        ActivityCompat.requestPermissions(this, LOCATION_PERMISSION, requestCode)
    }
}
