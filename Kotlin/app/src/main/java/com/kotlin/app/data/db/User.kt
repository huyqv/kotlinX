package com.kotlin.app.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2018/7/5
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
@Entity(tableName = "users")
class User {

    /**
     * Only once constructor if define @PrimaryKey(autoGenerate = true)
     * And default value must be type
     * @PrimaryKey(autoGenerate = true)
     * @ColumnInfo(name = User.ID)
     * var id: Int? = null
     */
    @ColumnInfo(name = "user_id")
    @PrimaryKey
    val id: String

    @ColumnInfo(name = "user_token")
    var accessToken: String? = null

    @ColumnInfo(name = "user_first_name")
    var firstName: String = ""

    @ColumnInfo(name = "user_last_name")
    var lastName: String = ""

    @ColumnInfo(name = "user_gender")
    var gender: Byte = 1

    @ColumnInfo(name = "user_birth")
    var birth: Long = 0

    @ColumnInfo(name = "usr_avatar")
    var avatar: String? = null

    @ColumnInfo(name = "usr_wallpaper")
    var wallpaper: String? = null


    constructor(id: String) {
        this.id = id
    }

    @Dao
    interface DAO : BaseDao<User> {

        // Return type not be type value as nullable: Collection<Model>?
        // It get error  method not be implement
        @Query("SELECT * FROM users")
        fun data(): List<User>

        @Query("SELECT * FROM users")
        fun liveData(): LiveData<List<User>>

        @Query("SELECT * FROM users WHERE user_id = :id")
        fun get(id: String): User

    }

}