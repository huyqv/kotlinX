package com.huy.kotlin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.huy.kotlin.BuildConfig
import com.huy.kotlin.app.App
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
        version = BuildConfig.DATABASE_VERSION,
        exportSchema = false,
        entities = [User::class]
)
abstract class RoomHelper : RoomDatabase() {

    abstract val userDao: User.DAO

    companion object {

        val instance: RoomHelper by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            initDatabase(App.instance.applicationContext)
        }

        private fun initDatabase(context: Context): RoomHelper {
            return Room.databaseBuilder(context, RoomHelper::class.java, BuildConfig.ATIFACT)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
        }

    }

}