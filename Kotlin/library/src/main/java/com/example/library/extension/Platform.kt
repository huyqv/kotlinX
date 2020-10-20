package com.example.library.extension

import android.os.Build

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/11/03
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
fun hasFroyo(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO
}

fun hasGingerbread(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
}

fun hasHoneycomb(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
}

fun hasJellBean(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
}

fun hasKitKat(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
}

fun haLollipop(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
}

fun hasMarshmallow(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
}

fun hasNougat(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
}

fun hasOreo(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
}

fun hasPie(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
}