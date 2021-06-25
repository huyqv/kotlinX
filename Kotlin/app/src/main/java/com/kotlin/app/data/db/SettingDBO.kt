package com.kotlin.app.data.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "setting")
class SettingDBO {

    /**
     * Only once constructor if define @PrimaryKey(autoGenerate = true)
     * And default value must be type
     * @PrimaryKey(autoGenerate = true)
     * @ColumnInfo(name = User.ID)
     * var id: Int? = null
     */
    @PrimaryKey
    @ColumnInfo(name = "setting_id")
    var id: Int? = null //  id auto increments

    @ColumnInfo(name = "setting_image", typeAffinity = ColumnInfo.BLOB)
    var image: ByteArray? = null

    @ColumnInfo(name = "setting_key")
    lateinit var key: String

    @ColumnInfo(name = "setting_value")
    var value: String? = null

    @Dao
    interface DAO : BaseDao<SettingDBO>
}