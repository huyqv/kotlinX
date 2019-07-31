package com.huy.kotlin.data.local

import android.content.Context
import android.content.SharedPreferences
import com.huy.kotlin.BuildConfig
import com.huy.kotlin.app.App

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/1/2
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class SharedHelper private constructor() {

    interface ValueChangedListener {
        fun onSharedValueChanged(key: String)
    }

    @Volatile
    private var currentPref: String = BuildConfig.SHARED_PREF

    private var shared: SharedPreferences? = null

    fun scope(prefName: String): SharedHelper {
        if (prefName != currentPref) {
            shared = App.instance.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            currentPref = prefName
        }
        return this
    }

    fun edit(block: SharedPreferences.Editor.() -> Unit) {
        shared?.edit()?.block()
        commit()
    }

    fun clear() {
        shared?.edit()?.clear()
        commit()
    }

    fun commit() {
        shared?.edit()?.apply()
    }

    fun addListener(listener: ValueChangedListener) {
        shared!!.registerOnSharedPreferenceChangeListener { sharedPref, key ->
            if (sharedPref != null && key != null)
                listener.onSharedValueChanged(key)
        }
    }

    fun removeListener(listener: ValueChangedListener) {
        shared!!.unregisterOnSharedPreferenceChangeListener { sharedPref, key ->
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

    fun stringOf(key: String): String? {
        return shared!!.getString(key, null)
    }

    fun intOf(key: String): Int {
        return shared!!.getInt(key, -1)
    }

    fun longOf(key: String): Long {
        return shared!!.getLong(key, -1)
    }

    fun booleanOf(key: String): Boolean {
        return shared!!.getBoolean(key, false)
    }

    companion object {
        val instance: SharedHelper by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            SharedHelper()
        }
    }
}