package com.example.library.extension

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/02/24
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
const val VOICE_REQUEST_CODE = 1005

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

fun <T : Activity> Fragment.start(cls: KClass<T>) {
    requireActivity().start(cls)
}

fun <T : Activity> Activity.start(cls: KClass<T>) {
    startActivity(Intent(this, cls.java))
}

fun Activity.navigateEmail() {
    startActivity(emailIntent)
}

fun Activity.startVoiceRecord(code: Int = VOICE_REQUEST_CODE) {
    startActivityForResult(voiceRecordIntent, code)
}

fun Activity.navigateSettings(code: Int = 0) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivityForResult(intent, code)
}

fun Activity.navigateCHPlay() {

    try {
        val s = "market://details?id=${applicationContext.packageName}"
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(s)))
    } catch (ex: android.content.ActivityNotFoundException) {
        val s = "https://play.google.com/store/apps/details?id=${applicationContext.packageName}"
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(s)))
    }
}

fun Activity.navigateBrowser(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}

/**
 * include in dependency [androidx.activity:activity-ktx:X.X.X]
 */
class IntentResultLauncher {

    private var launcher: ActivityResultLauncher<Intent>? = null

    fun observer(activity: ComponentActivity, callBack: (Intent) -> Unit) {

        val startActivityForResult = ActivityResultContracts.StartActivityForResult()

        val resultCallback = ActivityResultCallback<ActivityResult> {
            val data: Intent = it?.data ?: return@ActivityResultCallback
            callBack(data)
        }

        activity.lifecycle.addObserver(object : SimpleLifecycleObserver() {
            override fun onCreated() {
                launcher = activity.registerForActivityResult(startActivityForResult, resultCallback)
            }

            override fun onDestroy() {
                launcher?.unregister()
            }
        })
    }

    fun observer(fragment: Fragment, callBack: (Intent) -> Unit) {

        val startActivityForResult = ActivityResultContracts.StartActivityForResult()

        val resultCallback = ActivityResultCallback<ActivityResult> { result ->
            val data: Intent = result?.data ?: return@ActivityResultCallback
            callBack(data)
        }

        fragment.lifecycle.addObserver(object : SimpleLifecycleObserver() {
            override fun onCreated() {
                launcher = fragment.registerForActivityResult(startActivityForResult, resultCallback)
            }

            override fun onDestroy() {
                launcher?.unregister()
            }
        })
    }

}
