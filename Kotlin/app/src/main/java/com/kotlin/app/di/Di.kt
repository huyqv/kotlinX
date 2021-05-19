package com.kotlin.app.di

import androidx.room.Room
import com.kotlin.app.BuildConfig
import com.kotlin.app.app.App
import com.kotlin.app.data.db.RoomDB
import com.kotlin.app.data.network.ApiClient
import com.kotlin.app.data.network.ApiService

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2021/05/17
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
val apiClient: ApiClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    ApiClient()
}

val apiService: ApiService get() = apiClient.service

val room: RoomDB by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    Room.databaseBuilder(App.instance.applicationContext, RoomDB::class.java, BuildConfig.APPLICATION_ID)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
}

