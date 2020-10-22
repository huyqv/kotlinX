package com.example.kotlin.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kotlin.App
import com.example.kotlin.BuildConfig
import com.example.kotlin.data.entity.User

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/1/2
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
val room: RoomDB by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    Room.databaseBuilder(App.instance.applicationContext, RoomDB::class.java, BuildConfig.APPLICATION_ID)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
}

@Database(
        entities = [User::class],
        version = BuildConfig.DATABASE_VERSION,
        exportSchema = false
)
abstract class RoomDB : RoomDatabase() {

    abstract val userDao: User.DAO


}