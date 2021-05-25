package com.example.library.extension

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.IntDef
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.example.library.Library
import kotlin.collections.filter
import kotlin.collections.forEach
import kotlin.collections.isNullOrEmpty
import kotlin.collections.mutableMapOf
import kotlin.collections.set
import kotlin.collections.toTypedArray

private val app: Application get() = Library.app

fun ComponentActivity.onGranted(@RequiresPermission vararg permissions: String, block: () -> Unit) {
    val notGrantedPermissions = permissions.filter {
        ContextCompat.checkSelfPermission(app, it) != PackageManager.PERMISSION_GRANTED
    }
    if (notGrantedPermissions.isNullOrEmpty()) {
        block()
        return
    }

    val permissionArray = notGrantedPermissions.toTypedArray()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions(permissionArray, 1)
    } else {
        ActivityCompat.requestPermissions(this, permissionArray, 1)
    }
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

@IntDef(PermissionStatus.GRANTED, PermissionStatus.DENIED, PermissionStatus.BLOCKED_OR_NEVER_ASKED)
annotation class PermissionStatus {
    companion object {
        const val GRANTED = 0
        const val DENIED = 1
        const val BLOCKED_OR_NEVER_ASKED = 2
    }

}

@PermissionStatus
fun ComponentActivity.permissionStatus(permission: String): Int {
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

fun ComponentActivity.onGrantedRequestedPermission(block: () -> Unit) {
    onGranted(*notGrantedPermission) { block() }
}

fun Fragment.onGrantedRequestedPermission(block: () -> Unit) {
    onGranted(*notGrantedPermission) { block() }
}


class RequestPermission {

    private var resultLauncher: ActivityResultLauncher<Array<String>>? = null

    private val liveData = MutableLiveData<Boolean>()

    private val onGrantedList = mutableMapOf<Int, () -> Unit>()

    constructor(activity: ComponentActivity) {
        activity.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreated() {
                resultLauncher = activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                    liveData.value = true
                }
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                resultLauncher?.unregister()
            }
        })
        liveData.observe(activity, Observer {
            onGrantedList.forEach { block ->
                block
            }
        })
    }

    constructor(fragment: Fragment) {
        fragment.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreated() {
                resultLauncher = fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

                    liveData.value = true
                }

            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                resultLauncher?.unregister()
            }
        })
        liveData.observe(fragment.viewLifecycleOwner, Observer {
            onGrantedList.forEach { block ->
                block
            }
        })
    }

    fun onGranted(code: Int, @RequiresPermission vararg permissions: String, block: () -> Unit) {
        onGrantedList[code] = {
            val notGrantedPermissions = permissions.filter {
                ContextCompat.checkSelfPermission(app, it) != PackageManager.PERMISSION_GRANTED
            }
            if (notGrantedPermissions.isNullOrEmpty()) {
                block()
            }
        }
        resultLauncher?.launch(permissions as Array<String>?)
        liveData.value = true
    }


}