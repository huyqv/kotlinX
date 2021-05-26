package com.example.library.extension

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.example.library.Library


/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/30
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
private val app: Application get() = Library.app

private val permissionObserverMap by lazy { mutableMapOf<Int, LifecycleObserver?>() }

fun hasPermission(vararg permissions: String): Boolean {
    permissions.forEach {
        if (ContextCompat.checkSelfPermission(app, it) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}

fun ComponentActivity.onGrantedPermission(requestCode: Int, vararg permissions: String, onGrantedPermission: () -> Unit) {

    val deniedPermissions = mutableListOf<String>()

    val blockedPermissions = mutableListOf<String>()

    for (permission in permissions) {
        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                continue
            }
            ActivityCompat.shouldShowRequestPermissionRationale(this, permission) -> {
                deniedPermissions.add(permission)
            }
            else -> {
                blockedPermissions.add(permission)
            }
        }
    }

    if (deniedPermissions.isNotEmpty()) {
        observerPermission(requestCode, *permissions) { onGrantedPermission() }
        ActivityCompat.requestPermissions(this, permissions, requestCode)
        return
    }

    if (blockedPermissions.isNotEmpty()) {
        observerPermission(requestCode, *permissions) { onGrantedPermission() }
        showDialogPermission(*blockedPermissions.toTypedArray())
        return
    }

    onGrantedPermission()
}

fun Fragment.onGrantedPermission(requestCode: Int, vararg permissions: String, onGrantedPermission: () -> Unit) {
    requireActivity().onGrantedPermission(requestCode, *permissions) {
        onGrantedPermission()
    }
}

fun LifecycleOwner.observerPermission(requestCode: Int, vararg permissions: String, block: () -> Unit) {
    permissionObserverMap[requestCode]?.also {
        lifecycle.removeObserver(it)
    }
    val observer = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            if (hasPermission(*permissions)) {
                lifecycle.removeObserver(this)
                block()
            }
        }
    }
    permissionObserverMap[requestCode] = observer
    lifecycle.addObserver(observer)
}

private fun ComponentActivity.showDialogPermission(vararg permissions: String) {
    val message = requirePermissionMessage(*permissions)
    AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("Close") { dialog, _ -> dialog.cancel() }
            .setNegativeButton("Setting") { dialog, _ ->
                navigateAppSettings()
                dialog.cancel()
            }
            .show()
}

private fun navigateAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", app.packageName, null)
    intent.data = uri
    app.startActivity(intent)
}

private fun requirePermissionMessage(vararg permissions: String): String {
    return StringBuilder().also {
        it.append("Permission:")
        permissions.iterator().forEach { permission ->
            val s = permission.replace("android.permission.", "")
                    .replace("_", " ").toLowerCase()
            it.append(" $s,")
        }
        it.deleteCharAt(it.lastIndex)
        it.removeSurrounding(",")
        it.append(" had been blocked")
    }.toString()
}

val locationPermission get() = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

val requestedPermission: Array<String>
    get() = app
            .packageManager
            .getPackageInfo(app.packageName, PackageManager.GET_PERMISSIONS)
            .requestedPermissions

val notGrantedPermission: Array<String>
    get() = requestedPermission
            .filter { ContextCompat.checkSelfPermission(app, it) != PackageManager.PERMISSION_GRANTED }
            .toTypedArray()

fun ComponentActivity.onGrantedRequiredPermission(onGrantedPermission: () -> Unit) {
    onGrantedPermission(34534, *notGrantedPermission){
        onGrantedPermission()
    }
}



