package com.kotlin.app.ui.main

import com.kotlin.app.ui.dialog.DialogVM


interface MainView {

    val mainActivity: MainActivity?

    val mainVM: MainVM

    val dialogVM: DialogVM

}