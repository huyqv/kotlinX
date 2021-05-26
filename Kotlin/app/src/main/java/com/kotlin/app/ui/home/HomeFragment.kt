package com.kotlin.app.ui.home

import android.Manifest
import com.example.library.extension.onGrantedPermission
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

        textView.setOnClickListener {
             onGrantedPermission(1,Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                textView.text = "camera and storage permission is granted"
            }
        }

    }

    override fun onLiveDataObserve() {
    }


}