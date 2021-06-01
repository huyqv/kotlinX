package com.kotlin.app.data.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy 2020/08/22
 * @Organize: Wee Digital
 * @Description: ...
 * All Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
@Entity(tableName = "table")
class Table {

    /**
     * Only once constructor if define @PrimaryKey(autoGenerate = true)
     * And default value must be type
     * @PrimaryKey(autoGenerate = true)
     * @ColumnInfo(name = User.ID)
     * var id: Int? = null
     */
    @PrimaryKey
    @ColumnInfo(name = "table_id")
    var id: Int? = null //  id auto increments

    @ColumnInfo(name = "table_image", typeAffinity = ColumnInfo.BLOB)
    var image: ByteArray? = null

    @Dao
    interface DAO : BaseDao<Table>
}