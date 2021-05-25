package com.example.library.extension

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
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

fun isGranted(@RequiresPermission vararg permission: String): Boolean {
    val notGrantedPermissions = permission.filter {
        ContextCompat.checkSelfPermission(app, it) != PackageManager.PERMISSION_GRANTED
    }
    return notGrantedPermissions.isNullOrEmpty()
}

fun Fragment.onGrantedPermission(@RequiresPermission vararg permissions: String, block: () -> Unit) {

    val deniedPermissions = mutableListOf<String>()

    val blockedPermissions = mutableListOf<String>()

    for (permission in permissions) {
        when {
            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> {
                continue
            }
            shouldShowRequestPermissionRationale(permission) -> {
                deniedPermissions.add(permission)
            }
            else -> {
                blockedPermissions.add(permission)
            }
        }
    }

    if (!deniedPermissions.isNullOrEmpty()) {
        @Suppress("DEPRECATION")
        requestPermissions(permissions, 1)
        return
    }

    if (!blockedPermissions.isNullOrEmpty()) {
        requireActivity().showDialogPermission(*blockedPermissions.toTypedArray())
        return
    }

    block()
}

fun Fragment.observerPermission(@RequiresPermission vararg permissions: String, block: () -> Unit) {
    lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            if (isGranted(*permissions)) {
                block()
                lifecycle.removeObserver(this)
            } else {
                onGrantedPermission(*permissions) {
                    block()
                }
            }
        }
    })
}

private fun ComponentActivity.showDialogPermission(@RequiresPermission vararg permissions: String) {
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

private fun requirePermissionMessage(@RequiresPermission vararg permissions: String): String {
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

