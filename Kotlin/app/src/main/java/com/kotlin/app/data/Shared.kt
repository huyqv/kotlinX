package com.kotlin.app.data

import android.content.Context
import android.content.SharedPreferences
import com.kotlin.app.app.App
import com.kotlin.app.BuildConfig

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/1/2
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
private val shared: SharedPreferences by lazy {
    App.instance.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
}

object Shared {

    fun edit(block: SharedPreferences.Editor.() -> Unit) {
        shared.edit().block()
        commit()
    }

    fun clear() {
        shared.edit().clear()
        commit()
    }

    fun commit() {
        shared.edit().apply()
    }

    fun addListener(listener: (String) -> Unit) {
        shared.registerOnSharedPreferenceChangeListener { sharedPref, key ->
            if (sharedPref != null && key != null) {
                listener(key)
            }
        }
    }

    fun save(key: String, value: String?) {
        edit { putString(key, value) }
    }

    fun save(key: String, value: Int?) {
        edit { putInt(key, value ?: -1) }
    }

    fun save(key: String, value: Long?) {
        edit { putLong(key, value ?: -1) }
    }

    fun save(key: String, value: Boolean?) {
        edit { putBoolean(key, value ?: false) }
    }

    fun str(key: String): String? {
        return shared.getString(key, null)
    }

    fun int(key: String): Int {
        return shared.getInt(key, -1)
    }

    fun long(key: String): Long {
        return shared.getLong(key, -1)
    }

    fun bool(key: String): Boolean {
        return shared.getBoolean(key, false)
    }

}