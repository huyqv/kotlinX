package template.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kotlin.app.dbVersion


@Database(
        entities = [Table::class],
        version = dbVersion,
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