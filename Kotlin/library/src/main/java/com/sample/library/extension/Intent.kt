package com.sample.library.extension

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
import com.sample.library.app
import kotlin.reflect.KClass

const val VOICE_REQUEST_CODE = 1005

fun <T : Activity> Fragment.start(cls: KClass<T>) {
    requireActivity().start(cls)
}

fun <T : Activity> Activity.start(cls: KClass<T>) {
    startActivity(Intent(this, cls.java))
}

fun Activity.startVoiceRecord() {
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).also {
        it.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        it.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech")
    }
    startActivityForResult(intent, VOICE_REQUEST_CODE)
}

fun navigateEmail() {
    app.startActivity(Intent(Intent.ACTION_MAIN).also {
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        it.addCategory(Intent.CATEGORY_APP_EMAIL)
    })
}

fun navigateCHPlay() {
    try {
        app.startActivity(Intent(Intent.ACTION_VIEW).also {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            it.data = Uri.parse("market://details?id=${app.packageName}")
        })
    } catch (ex: android.content.ActivityNotFoundException) {
        app.startActivity(Intent(Intent.ACTION_VIEW).also {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            it.data = Uri.parse("https://play.google.com/store/apps/details?id=${app.packageName}")
        })
    }
}

fun navigateBrowser(url: String) {
    app.startActivity(Intent(Intent.ACTION_VIEW).also {
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        it.data = Uri.parse(url)
    })
}

fun navigateCall(phone: String) {
    app.startActivity(Intent(Intent.ACTION_DIAL).also {
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        it.data = Uri.parse("tel:$phone")
    })
}

fun navigateSettings() {
    app.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        it.data = Uri.fromParts("package", packageName, null)
    })
}

fun navigateDateSettings() {
    app.startActivity(Intent(Settings.ACTION_DATE_SETTINGS).also {
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    })
}

fun navigateAppSettings() {
    app.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        it.data = Uri.fromParts("package", app.packageName, null)
    })
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
