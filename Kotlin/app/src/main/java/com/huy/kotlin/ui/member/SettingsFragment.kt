package com.huy.kotlin.ui.member

import android.os.Build
import com.huy.kotlin.R
import com.huy.kotlin.base.view.BaseFragment
import com.huy.library.extension.androidId
import com.huy.library.extension.appVersion
import com.huy.library.extension.deviceName
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
        return R.layout.fragment_settings
    }

    override fun onViewCreated() {
        settingTextViewAppVersion.text = appVersion
        settingTextViewOsName.text = Build.VERSION.RELEASE
        settingTextViewSdkVersion.text = Build.VERSION.SDK_INT.toString()
        settingTextViewDeviceName.text = deviceName
        settingTextViewDeviceId.text = androidId
    }

}