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

private val permissionObserverMap by lazy { mutableMapOf<Int, LifecycleObserver>() }

private val requestedPermissionMap by lazy { mutableMapOf<String, Boolean>() }

fun hasPermission(vararg permissions: String): Boolean {
    permissions.forEach {
        if (ContextCompat.checkSelfPermission(app, it) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}

fun FragmentActivity.onPermissionGranted(
    requestCode: Int,
    vararg permissions: String,
    onGranted: () -> Unit
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
        observerPermission(requestCode, *permissions) { onGranted() }
        requestedPermissionMap[this.localClassName] = true
        ActivityCompat.requestPermissions(this, notGrantedPermissions.toTypedArray(), requestCode)
        return
    }

    if (deniedPermissions.isNotEmpty()) {
        observerPermission(requestCode, *permissions) { onGranted() }
        if (requestedPermissionMap[this.localClassName] == true) {
            showDialogPermission(*deniedPermissions.toTypedArray())
        } else {
            requestedPermissionMap[this.localClassName] = true
            ActivityCompat.requestPermissions(this, deniedPermissions.toTypedArray(), requestCode)
        }
        return
    }

    onGranted()
}

fun Fragment.onPermissionGranted(
    requestCode: Int,
    vararg permissions: String,
    onGranted: () -> Unit
) {
    activity?.onPermissionGranted(requestCode, *permissions) {
        onGranted()
    }
}

fun FragmentActivity.observerPermission(
    requestCode: Int,
    vararg permissions: String,
    onGranted: () -> Unit
) {
    permissionObserverMap[requestCode]?.also {
        lifecycle.removeObserver(it)
    }
    val observer = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            if (hasPermission(*permissions)) {
                lifecycle.removeObserver(this)
                onGranted()
            }
        }
    }
    permissionObserverMap[requestCode] = observer
    lifecycle.addObserver(observer)
}

private fun FragmentActivity.showDialogPermission(vararg permissions: String) {
    AlertDialog.Builder(this)
        .setMessage("Permission:${permissionsText(*permissions)} had been denied")
        .setPositiveButton("Close") { dialog, _ -> dialog.cancel() }
        .setNegativeButton("Setting") { dialog, _ ->
            navigateAppSettings()
            dialog.cancel()
        }
        .show()
}

fun permissionsText(vararg permissions: String): String {
    return StringBuilder().also {
        permissions.iterator().forEach { permission ->
            val s = permission.replace("android.permission.", "")
                .replace("_", " ").toLowerCase()
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
    onPermissionGranted(34534, *notGrantedPermission) {
        onGrantedPermission()
    }
}



