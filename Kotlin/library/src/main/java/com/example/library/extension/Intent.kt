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
import com.example.library.Library
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

private val app get() = Library.app

fun <T : Activity> Fragment.start(cls: KClass<T>) {
    requireActivity().start(cls)
}

fun <T : Activity> Activity.start(cls: KClass<T>) {
    startActivity(Intent(this, cls.java))
}

fun Activity.startVoiceRecord() {
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech")
    }
    startActivityForResult(intent, VOICE_REQUEST_CODE)
}

fun navigateEmail() {
    val intent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_APP_EMAIL)
}
    app.startActivity(intent)
}

fun navigateCHPlay() {
    try {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.parse("market://details?id=${app.packageName}")
        }
        app.startActivity(intent)
    } catch (ex: android.content.ActivityNotFoundException) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            data = Uri.parse("https://play.google.com/store/apps/details?id=${app.packageName}")
        }
        app.startActivity(intent)
    }
}

fun navigateBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        data = Uri.parse(url)
    }
    app.startActivity(intent)
}

fun navigateCall(phone: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        data = Uri.parse("tel:$phone")
    }
    app.startActivity(intent)
}

fun navigateSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        data = Uri.fromParts("package", packageName, null)
    }
    app.startActivity(intent)
}

fun navigateDateSettings() {
    val intent = Intent(Settings.ACTION_DATE_SETTINGS).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    app.startActivity(intent)
}

fun navigateAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        data = Uri.fromParts("package", app.packageName, null)
    }
    app.startActivity(intent)
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
