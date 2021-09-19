package com.sample.library.extension

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.sample.library.app
import java.util.*

val locationPermission
    get() = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION)


fun isGranted(vararg permissions: String): Boolean {
    permissions.iterator().forEach {
        if (ContextCompat.checkSelfPermission(app, it) == PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}

/**
 *
 */
fun FragmentActivity.onGranted(
        vararg permissions: String,
        onGranted: () -> Unit,
        onDenied: (List<String>) -> Unit
) {
    val deniedPermissions = mutableListOf<String>()
    val notGrantedPermissions = mutableListOf<String>()
    for (permission in permissions) {
        when {
            isGranted(permission) -> {
                continue
            }
            ActivityCompat.shouldShowRequestPermissionRationale(this, permission) -> {
                deniedPermissions.add(permission)
            }
            else -> {
                notGrantedPermissions.add(permission)
            }
        }
    }
    if (notGrantedPermissions.isNotEmpty()) {
        ActivityCompat.requestPermissions(this, notGrantedPermissions.toTypedArray(), 101)
        return
    }
    if (deniedPermissions.isNotEmpty()) {
        onDenied.invoke(deniedPermissions.toList())
        return
    }
    onGranted()
}

fun Fragment.onGranted(
        vararg permissions: String,
        onGranted: () -> Unit,
        onDenied: (List<String>) -> Unit
) {
    activity?.onGranted(permissions = permissions, onGranted = onGranted, onDenied = onDenied)
}

fun FragmentActivity.onGranted(vararg permissions: String, onGranted: () -> Unit) {
    onGranted(permissions = permissions, onGranted, {
        showDialogPermission(it)
    })
}

fun Fragment.onGranted(vararg permissions: String, onGranted: () -> Unit) {
    onGranted(permissions = permissions, onGranted, {
        requireActivity().showDialogPermission(it)
    })
}

fun FragmentActivity.observerPermission(vararg permissions: String, onGranted: () -> Unit) {
    if (isGranted(*permissions)) {
        onGranted()
        return
    }
    lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            if (isGranted(*permissions)) {
                lifecycle.removeObserver(this)
                onGranted()
            }
        }
    })
    onGranted(permissions = permissions, onGranted) {
        showDialogPermission(it)
    }
}

fun Fragment.observerPermission(vararg permissions: String, onGranted: () -> Unit) {
    if (isGranted(*permissions)) {
        onGranted()
        return
    }
    lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            if (isGranted(*permissions)) {
                lifecycle.removeObserver(this)
                onGranted()
            }
        }
    })
    onGranted(permissions = permissions, onGranted) {
        requireActivity().showDialogPermission(it)
    }
}

private fun FragmentActivity.showDialogPermission(permissions: List<String>) {
    val sb = StringBuilder()
    permissions.iterator().forEach { permission ->
        val s = permission.replace("android.permission.", "")
                .replace("_", " ").lowercase(Locale.getDefault())
        sb.append(" $s,")
    }
    sb.deleteCharAt(sb.lastIndex)
    val permissionsText = sb.toString()
    AlertDialog.Builder(this)
            .setMessage("Permission:$permissionsText had been denied")
            .setPositiveButton("Close") { dialog, _ -> dialog.cancel() }
            .setNegativeButton("Setting") { dialog, _ ->
                dialog.cancel()
                this.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    it.data = Uri.fromParts("package", this.packageName, null)
                })
            }.show()
}

private fun requestedPermission(activity: Activity): Array<String> {
    return activity.packageManager
            .getPackageInfo(app.packageName, PackageManager.GET_PERMISSIONS)
            .requestedPermissions
}

fun FragmentActivity.onGrantedRequiredPermission(block: () -> Unit) {
    onGranted(*requestedPermission(this), onGranted = block)
}

fun Fragment.onGrantedRequiredPermission(block: () -> Unit) {
    onGranted(*requestedPermission(requireActivity()), onGranted = block)
}



