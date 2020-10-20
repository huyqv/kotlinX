package com.example.library.extension

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/30
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun Activity.onGranted(@RequiresPermission vararg permissions: String, onGranted: () -> Unit) {
    val list = mutableListOf<String>()
    permissions.forEach {
        if (ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED) {
            list.add(it)
        }
    }
    if (list.isNullOrEmpty()) {
        onGranted()
        return
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions(permissions, 1)
    } else {
        ActivityCompat.requestPermissions(this, permissions, 1)
    }

}

fun Activity.isGranted(@RequiresPermission vararg permission: String): Boolean {
    val list = mutableListOf<String>()
    permission.forEach {
        if (ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED) {
            list.add(it)
        }
    }
    return list.isNullOrEmpty()
}

fun FragmentActivity.onGrantedPermission(permission: String, block: () -> Unit) {
    RxPermissions(this)
            .request(permission)
            .subscribe { granted -> if (granted) block() }
}

fun Fragment.onGranted(@RequiresPermission vararg permissions: String, onGranted: () -> Unit) {
    val list = mutableListOf<String>()
    permissions.forEach {
        if (ContextCompat.checkSelfPermission(context!!, it) != PackageManager.PERMISSION_GRANTED) {
            list.add(it)
        }
    }
    if (list.isNullOrEmpty()) {
        onGranted()
        return
    }
    requestPermissions(permissions, 1)
}

fun Fragment.isGranted(@RequiresPermission vararg permission: String): Boolean {
    val list = mutableListOf<String>()
    permission.forEach {
        if (ContextCompat.checkSelfPermission(context!!, it) != PackageManager.PERMISSION_GRANTED) {
            list.add(it)
        }
    }
    return list.isNullOrEmpty()
}

fun Fragment.onGrantedPermission(permission: String, block: () -> Unit) {
    RxPermissions(this)
            .request(permission)
            .subscribe { granted -> if (granted) block() }
}








