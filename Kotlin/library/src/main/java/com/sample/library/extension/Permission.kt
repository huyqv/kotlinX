package com.sample.library.extension

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.sample.library.app
import java.util.*

val locationPermission
    get() = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

private fun requestedPermission(): Array<String> {
    return app.packageManager
        .getPackageInfo(app.packageName, PackageManager.GET_PERMISSIONS)
        .requestedPermissions
}

fun isGranted(vararg permissions: String): Boolean {
    permissions.iterator().forEach {
        if (ContextCompat.checkSelfPermission(app, it) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}

/**
 *
 */
fun LifecycleOwner.onGrantedPermission(
    vararg permissions: String,
    onGranted: () -> Unit,
    onDenied: (List<String>) -> Unit
) {
    val deniedPermissions = mutableListOf<String>()
    val notGrantedPermissions = mutableListOf<String>()
    val activity = requireActivity() ?: return
    for (permission in permissions) {
        when {
            isGranted(permission) -> {
                continue
            }
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) -> {
                deniedPermissions.add(permission)
            }
            else -> {
                notGrantedPermissions.add(permission)
            }
        }
    }
    if (notGrantedPermissions.isNotEmpty()) {
        ActivityCompat.requestPermissions(activity, notGrantedPermissions.toTypedArray(), 101)
        return
    }
    if (deniedPermissions.isNotEmpty()) {
        onDenied.invoke(deniedPermissions.toList())
        return
    }
    onGranted()
}

fun LifecycleOwner.onGrantedPermission(vararg permissions: String, onGranted: () -> Unit) {
    onGrantedPermission(permissions = permissions, onGranted, {
        showDialogPermission(it)
    })
}

fun LifecycleOwner.observerPermission(vararg permissions: String, onGranted: () -> Unit) {
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
    onGrantedPermission(permissions = permissions, onGranted) {
        showDialogPermission(it)
    }
}

private fun LifecycleOwner.showDialogPermission(permissions: List<String>) {
    val sb = StringBuilder()
    permissions.iterator().forEach { permission ->
        val s = permission.replace("android.permission.", "")
            .replace("_", " ").lowercase(Locale.getDefault())
        sb.append(" $s,")
    }
    sb.deleteCharAt(sb.lastIndex)
    val permissionsText = sb.toString()
    val activity = requireActivity() ?: return
    AlertDialog.Builder(activity)
        .setMessage("Permission:$permissionsText had been denied")
        .setPositiveButton("Close") { dialog, _ -> dialog.cancel() }
        .setNegativeButton("Setting") { dialog, _ ->
            dialog.cancel()
            activity.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                it.data = Uri.fromParts("package", activity.packageName, null)
            })
        }.show()
}

fun LifecycleOwner.onGrantedRequiredPermission(block: () -> Unit) {
    onGrantedPermission(*requestedPermission(), onGranted = block)
}

fun Fragment.observerCameraPermission(onGranted: () -> Unit) {
    val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                onGranted()
            }
        }
    val permission = android.Manifest.permission.CAMERA
    when {
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED -> {
            onGranted()
        }
        shouldShowRequestPermissionRationale(permission) -> {
            AlertDialog.Builder(requireActivity())
                .setMessage("Quyền truy cập camera bị từ chối")
                .setPositiveButton("Đóng") { dialog, _ -> dialog.cancel() }
                .setNegativeButton("Setting") { dialog, _ ->
                    dialog.cancel()
                    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        it.data = Uri.fromParts("package", requireActivity().packageName, null)
                    })
                }.show()
        }
        else -> {
            permissionLauncher.launch(permission)
        }
    }
}



