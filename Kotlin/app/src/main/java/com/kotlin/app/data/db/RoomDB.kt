package com.kotlin.app.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kotlin.app.BuildConfig
import com.kotlin.app.app.App

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/1/2
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
@Database(
        entities = [User::class],
        version = BuildConfig.DATABASE_VERSION,
        exportSchema = false
)
abstract class RoomDB : RoomDatabase() {

    abstract val userDao: User.DAO

    companion object {
        val instance: RoomDB by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            Room.databaseBuilder(App.instance.applicationContext, RoomDB::class.java, BuildConfig.APPLICATION_ID)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
        }

    }
}