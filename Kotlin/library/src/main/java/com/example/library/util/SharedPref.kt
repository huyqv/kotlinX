package com.example.library.util

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.library.Library


/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/10/16
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class SharedPref(private val fileName: String) {

    private val context get() = Library.app

    private val pref: SharedPreferences by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) try {
            val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            EncryptedSharedPreferences.create(
                    fileName,
                    masterKey,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
        }
        context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }

    fun edit(block: SharedPreferences.Editor.() -> Unit) {
        val edit = pref.edit()
        edit.block()
        edit.apply()
    }

    fun str(key: String, default: String? = null): String? = pref.getString(key, default)

    fun long(key: String, default: Long = -1): Long = pref.getLong(key, -default)

    fun int(key: String, default: Int = -1): Int = pref.getInt(key, default)

    fun bool(key: String, default: Boolean = false): Boolean = pref.getBoolean(key, default)

}