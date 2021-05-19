package com.example.library.util

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.library.BuildConfig
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
object SharedAll {

    private val context get() = Library.app

    private val pref: SharedPreferences by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val masterKey = MasterKey.Builder(context)
                    .setKeyGenParameterSpec(specSDK23())
                    .build()
            EncryptedSharedPreferences.create(
                    context,
                    BuildConfig.LIBRARY_PACKAGE_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM) as EncryptedSharedPreferences
        } else {
            context.getSharedPreferences(BuildConfig.LIBRARY_PACKAGE_NAME, Context.MODE_PRIVATE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun specSDK23(): KeyGenParameterSpec {
        val builder = KeyGenParameterSpec.Builder("_androidx_security_master_key_",
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                builder.setUnlockedDeviceRequired(true)
                builder.setIsStrongBoxBacked(true)
            } catch (ignore: Exception) {
            }
        }
        return builder.build()
    }

    fun edit(block: SharedPreferences.Editor.() -> Unit) {
        val edit = pref.edit()
        edit.block()
        edit.commit()
        edit.apply()
    }

    fun str(key: String, default: String? = null): String? = pref.getString(key, default)

    fun long(key: String, default: Long = -1): Long = pref.getLong(key, -default)

    fun int(key: String, default: Int = -1): Int = pref.getInt(key, default)

    fun bool(key: String, default: Boolean = false): Boolean = pref.getBoolean(key, default)

}