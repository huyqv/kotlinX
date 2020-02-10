package com.huy.library.extension

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.speech.RecognizerIntent
import androidx.fragment.app.Fragment


/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/02/24
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
const val IMAGE_REQUEST_CODE = 1004

const val VOICE_REQUEST_CODE = 1005

val galleryIntent: Intent
    get() {
        val intent = Intent(Intent.ACTION_PICK)
        val directory = android.os.Environment
                .getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_PICTURES)
        val path = directory.path
        val data = Uri.parse(path)
        intent.setDataAndType(data, "image/*")
        return intent
    }

val voiceRecordIntent: Intent
    get() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech")
        return intent
    }

val emailIntent: Intent
    get() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_APP_EMAIL)
        return intent
    }

fun Activity.startSettings() {
    startActivityForResult(Intent(Settings.ACTION_SETTINGS), 0)
}

fun Activity.startEmail() {
    startActivity(emailIntent)
}

fun Activity.startVoiceRecord(code: Int = VOICE_REQUEST_CODE) {
    startActivityForResult(voiceRecordIntent, code)
}

fun Activity.startAppSettings(code: Int) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivityForResult(intent, code)
}

fun Activity.navigateAppStore() {

    try {
        val s = "market://details?id=${applicationContext.packageName}"
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(s)))
    } catch (ex: android.content.ActivityNotFoundException) {
        val s = "https://play.google.com/store/apps/details?id=${applicationContext.packageName}"
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(s)))
    }
}

fun Fragment.startSettings() {
    startActivityForResult(Intent(Settings.ACTION_SETTINGS), 0)
}

fun Fragment.startEmail() {
    startActivity(emailIntent)
}

fun Fragment.startBrowser(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}

fun Fragment.startVoiceRecord(code: Int = VOICE_REQUEST_CODE) {
    startActivityForResult(voiceRecordIntent, code)
}

fun Fragment.startAppStore() {
    activity?.navigateAppStore()
}

fun Fragment.startAppSettings(code: Int) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", context?.packageName, null)
    intent.data = uri
    startActivityForResult(intent, code)
}
