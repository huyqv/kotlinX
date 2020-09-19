package com.huy.kotlin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.huy.kotlin.App
import com.huy.kotlin.BuildConfig
import com.huy.kotlin.data.entity.User

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
            initDatabase(App.instance.applicationContext)
        }

        private fun initDatabase(context: Context): RoomDB {
            return Room.databaseBuilder(context, RoomDB::class.java, BuildConfig.APPLICATION_ID)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
        }

    }

}