package com.example.library.extension

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.IntDef
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/30
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */


@IntDef(PermissionStatus.GRANTED, PermissionStatus.DENIED, PermissionStatus.BLOCKED_OR_NEVER_ASKED)
annotation class PermissionStatus {
    companion object {
        const val GRANTED = 0
        const val DENIED = 1
        const val BLOCKED_OR_NEVER_ASKED = 2
    }

}

@PermissionStatus
fun Activity.permissionStatus(permission: String): Int {
    return when {
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
            PermissionStatus.GRANTED
        }
        ActivityCompat.shouldShowRequestPermissionRationale(this, permission) -> {
            PermissionStatus.DENIED
        }
        else -> {
            PermissionStatus.BLOCKED_OR_NEVER_ASKED
        }
    }
}

@PermissionStatus
fun Fragment.permissionStatus(permission: String): Int {
    return requireActivity().permissionStatus(permission)
}

fun Activity.isGranted(@RequiresPermission vararg permission: String): Boolean {
    val list = mutableListOf<String>()
    permission.iterator().forEach {
        if (permissionStatus(it) != PermissionStatus.GRANTED) {
            list.add(it)
        }
    }
    return list.isNullOrEmpty()
}

fun Fragment.isGranted(@RequiresPermission vararg permission: String): Boolean {
    return requireActivity().isGranted(* permission)
}

fun Activity.onGranted(@RequiresPermission vararg permissions: String, onGranted: () -> Unit) {
    if (isGranted(*permissions)) {
        onGranted()
        return
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions(permissions, 1)
    } else {
        ActivityCompat.requestPermissions(this, permissions, 1)
    }
}

fun Fragment.onGranted(@RequiresPermission vararg permissions: String, onGranted: () -> Unit) {
    if (isGranted(*permissions)) {
        onGranted()
        return
    }
    requestPermissions(permissions, 1)
}

val Activity.hasCameraPermission: Boolean get() = isGranted(Manifest.permission.CAMERA)

val Fragment.hasCameraPermission: Boolean get() = isGranted(Manifest.permission.CAMERA)

fun Activity.onCameraPermissionGranted(onGranted: () -> Unit) {
    this.onGranted(Manifest.permission.CAMERA) { onGranted() }
}

fun Fragment.onCameraPermissionGranted(onGranted: () -> Unit) {
    this.onGranted(Manifest.permission.CAMERA) { onGranted() }
}

val locationPermission get() = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

val Activity.hasLocationPermission: Boolean get() = isGranted(*locationPermission)

val Fragment.hasLocationPermission: Boolean get() = isGranted(*locationPermission)

fun Activity.onLocationPermissionGranted(onGranted: () -> Unit) {
    this.onGranted(*locationPermission) { onGranted() }
}

fun Fragment.onLocationPermissionGranted(onGranted: () -> Unit) {
    this.onGranted(*locationPermission) { onGranted() }
}









