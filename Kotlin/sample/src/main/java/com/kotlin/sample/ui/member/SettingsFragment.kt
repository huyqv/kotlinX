package com.kotlin.sample.ui.member

import android.os.Build
import com.kotlin.sample.R
import com.kotlin.sample.base.view.BaseFragment
import com.example.library.extension.androidId
import com.example.library.extension.appVersion
import com.example.library.extension.deviceName
import kotlinx.android.synthetic.main.fragment_settings.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/09/28
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class SettingsFragment : BaseFragment() {

    override fun layoutResource(): Int {
        return R.layout.sample_settings
    }

    override fun onViewCreated() {
        settingTextViewAppVersion.text = appVersion
        settingTextViewOsName.text = Build.VERSION.RELEASE
        settingTextViewSdkVersion.text = Build.VERSION.SDK_INT.toString()
        settingTextViewDeviceName.text = deviceName
        settingTextViewDeviceId.text = androidId
    }

    override fun onLiveDataObserve() {
    }
}