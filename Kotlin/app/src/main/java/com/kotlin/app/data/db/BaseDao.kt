package com.kotlin.app.data.db

import androidx.room.*

@Dao
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(model: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(coll: Collection<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(array: Array<T>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(t: T)

    @Delete
    fun delete(t: T)

}