package com.kotlin.app.ui.home

import android.Manifest
import com.example.library.extension.RequestPermission
import com.example.library.extension.onGranted
import com.example.library.extension.post
import com.example.library.extension.toast
import com.kotlin.app.MainDirections
import com.kotlin.app.R
import com.kotlin.app.ui.dialog.alert.AlertArg
import com.kotlin.app.ui.main.MainFragment
import kotlinx.android.synthetic.main.home.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/11/01
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class HomeFragment : MainFragment() {

    //private val requestPermission = RequestPermission(this)

    override fun layoutResource(): Int {
        return R.layout.home
    }

    override fun onViewCreated() {
        textView.setOnClickListener {
            dialogVM.alertLiveData.value = AlertArg(
                    message = "sample request camera and storage permission",
                    buttonPositive = "Do if all is granted",
                    onPositiveClick = {
                        onGranted(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                            textView.text = "camera and storage permission is granted"
                        }
                    },
                    onDismiss = {
                        textView.text = "dismiss"
                    }
            )
            navigate(MainDirections.actionGlobalAlertFragment())
        }
    }

    override fun onLiveDataObserve() {
    }

    override fun onResume() {
        super.onResume()
        toast("on Resume")
    }

}