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
@Entity(tableName = "templates")
class Template {

    @PrimaryKey
    @ColumnInfo(name = "template_id")
    var id: Int? = null //  id auto increments

    @ColumnInfo(name = "template_image", typeAffinity = ColumnInfo.BLOB)
    var image: ByteArray? = null

    @Dao
    interface DAO : BaseDao<Template>
}