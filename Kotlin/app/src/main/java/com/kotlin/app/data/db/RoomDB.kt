package com.kotlin.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kotlin.app.shared.dbVersion


@Database(
    entities = [SettingDBO::class],
    version = dbVersion,
    exportSchema = false
)
abstract class RoomDB : RoomDatabase() {

    abstract val setting: SettingDBO.DAO

    companion object {

        fun getInstance(context: Context, name: String): RoomDB {
            return Room.databaseBuilder(context.applicationContext, RoomDB::class.java, name)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}