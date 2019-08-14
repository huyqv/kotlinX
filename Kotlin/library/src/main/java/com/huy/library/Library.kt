package com.huy.library

import android.app.Application

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/08/10
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
object Library {

    private lateinit var extension: Extension

    val app: Application get() = extension.app()

    fun inject(ext: Extension) {
        extension = ext
    }

    interface Extension {
        fun app(): Application {
            return this as? Application
                    ?: throw ClassCastException("Implementation of Library must be a android.app.Application")
        }
    }

}
