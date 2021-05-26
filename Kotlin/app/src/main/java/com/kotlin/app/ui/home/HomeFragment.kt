package com.kotlin.app.ui.home

import android.Manifest
import com.example.library.extension.hasPermission
import com.example.library.extension.navigateAppSettings
import com.example.library.extension.onPermissionGranted
import com.kotlin.app.R
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

    override fun layoutResource(): Int {
        return R.layout.home
    }

    override fun onViewCreated() {
        val samplePermission = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (hasPermission(*samplePermission)) {
            textView.text = "all permission was granted"
        } else {
            textView.text = "has permission was denied"
        }
        textView.setOnClickListener {
            if (hasPermission(*samplePermission)) {
                navigateAppSettings()
            } else onPermissionGranted(1, *samplePermission) {
                textView.text = "all permission was granted"
            }
        }

    }

    override fun onLiveDataObserve() {
    }


}