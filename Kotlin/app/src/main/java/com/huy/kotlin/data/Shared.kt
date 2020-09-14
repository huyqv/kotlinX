package com.huy.kotlin.data

import android.content.Context
import android.content.SharedPreferences
import com.huy.kotlin.BuildConfig
import com.huy.kotlin.App

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/1/2
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class Shared private constructor() {

    interface ValueChangedListener {
        fun onSharedValueChanged(key: String)
    }

    private val shared: SharedPreferences by lazy {
        App.instance.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
    }

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

    fun addListener(listener: ValueChangedListener) {
        shared.registerOnSharedPreferenceChangeListener { sharedPref, key ->
            if (sharedPref != null && key != null)
                listener.onSharedValueChanged(key)
        }
    }

    fun removeListener(listener: ValueChangedListener) {
        shared.unregisterOnSharedPreferenceChangeListener { sharedPref, key ->
            if (sharedPref != null && key != null)
                listener.onSharedValueChanged(key)
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

    companion object {
        val instance: Shared by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            Shared()
        }
    }

}