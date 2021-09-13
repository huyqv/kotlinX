package com.sample.library.extension

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
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

private val permissionObserverMap by lazy { mutableMapOf<Int, LifecycleObserver>() }

private val requestedPermissionMap by lazy { mutableMapOf<String, Boolean>() }

fun isGranted(vararg permissions: String): Boolean {
    permissions.forEach {
        if (ContextCompat.checkSelfPermission(app, it) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}

fun isNotGranted(vararg permissions: String): Boolean {
    return !isGranted(*permissions)
}

/**
 *
 */
fun FragmentActivity.onGranted(
    vararg permissions: String,
    onGranted: () -> Unit,
    onDenied: (List<String>) -> Unit
) {

    val notGrantedPermissions = mutableListOf<String>()

    val deniedPermissions = mutableListOf<String>()

    for (permission in permissions) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                continue
            }
            ActivityCompat.shouldShowRequestPermissionRationale(this, permission) -> {
                notGrantedPermissions.add(permission)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(
                permission
            ) -> {
                notGrantedPermissions.add(permission)
            }
            else -> {
                deniedPermissions.add(permission)
            }
        }
    }

    if (notGrantedPermissions.isNotEmpty()) {
        //observerPermission(1, *permissions) { onGranted() }
        requestedPermissionMap[this.localClassName] = true
        ActivityCompat.requestPermissions(this, notGrantedPermissions.toTypedArray(), 1)
        return
    }

    if (deniedPermissions.isNotEmpty()) {
        //observerPermission(1, *permissions) { onGranted() }
        if (requestedPermissionMap[this.localClassName] == true) {
            onDenied(deniedPermissions.toList())
        } else {
            requestedPermissionMap[this.localClassName] = true
            ActivityCompat.requestPermissions(this, deniedPermissions.toTypedArray(), 1)
        }
        return
    }

    onGranted()
}

fun Fragment.onGranted(
    vararg permissions: String,
    onGranted: () -> Unit,
    onDenied: (List<String>) -> Unit
) {
    activity?.onGranted(
        permissions = permissions,
        onGranted = onGranted,
        onDenied = onDenied
    )
}

/**
 *
 */
fun FragmentActivity.onGranted(
    vararg permissions: String,
    onGranted: () -> Unit
) {
    onGranted(permissions = permissions, onGranted, {
        showDialogPermission(it)
    })
}

fun Fragment.onGranted(
    vararg permissions: String,
    onGranted: () -> Unit
) {
    activity?.onGranted(permissions = permissions, onGranted = onGranted)
}

/**
 *
 */
fun Lifecycle.observerPermission(
    requestCode: Int,
    vararg permissions: String,
    onGranted: () -> Unit
) {
    if (isGranted(*permissions)) {
        onGranted()
        return
    }
    permissionObserverMap[requestCode]?.also {
        removeObserver(it)
    }
    val observer = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            if (isGranted(*permissions)) {
                removeObserver(this)
                onGranted()
            }
        }
    }
    permissionObserverMap[requestCode] = observer
    addObserver(observer)
}

fun FragmentActivity.observerPermission(
    requestCode: Int,
    vararg permissions: String,
    onGranted: () -> Unit
) {
    lifecycle.observerPermission(requestCode, permissions = permissions, onGranted)
}

fun Fragment.observerPermission(
    requestCode: Int,
    vararg permissions: String,
    onGranted: () -> Unit
) {
    lifecycle.observerPermission(requestCode, permissions = permissions, onGranted)
}

private fun FragmentActivity.showDialogPermission(permissions: List<String>) {
    AlertDialog.Builder(this)
        .setMessage("Permission:${permissionsText(permissions)} had been denied")
        .setPositiveButton("Close") { dialog, _ -> dialog.cancel() }
        .setNegativeButton("Setting") { dialog, _ ->
            navigateAppSettings()
            dialog.cancel()
        }
        .show()
}

fun permissionsText(permissions: List<String>): String {
    return StringBuilder().also {
        permissions.iterator().forEach { permission ->
            val s = permission.replace("android.permission.", "")
                .replace("_", " ").lowercase(Locale.getDefault())
            it.append(" $s,")
        }
        it.deleteCharAt(it.lastIndex)
    }.toString()
}

val locationPermission
    get() = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

val requestedPermission: Array<String>
    get() = app
        .packageManager
        .getPackageInfo(app.packageName, PackageManager.GET_PERMISSIONS)
        .requestedPermissions

val notGrantedPermission: Array<String>
    get() = requestedPermission
        .filter { ContextCompat.checkSelfPermission(app, it) != PackageManager.PERMISSION_GRANTED }
        .toTypedArray()

fun FragmentActivity.onGrantedRequiredPermission(onGrantedPermission: () -> Unit) {
    onGranted(*notGrantedPermission) {
        onGrantedPermission()
    }
}



