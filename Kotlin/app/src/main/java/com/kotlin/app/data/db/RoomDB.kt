package com.kotlin.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kotlin.app.BuildConfig

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/1/2
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
@Database(
        entities = [Table::class],
        version = BuildConfig.DATABASE_VERSION,
        exportSchema = false
)
abstract class RoomDB : RoomDatabase() {

    abstract val holder: Table.DAO

    companion object {

        fun getInstance(context: Context, name: String): RoomDB {
            return Room.databaseBuilder(context.applicationContext, RoomDB::class.java, name)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }
}