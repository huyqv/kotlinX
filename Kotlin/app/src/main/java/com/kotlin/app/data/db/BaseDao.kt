package com.kotlin.app.data.db

import androidx.room.*

@Dao
interface BaseDao<Model> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(model: Model)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(coll: Collection<Model>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(array: Array<Model>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(t: Model)

    @Delete
    fun delete(t: Model)

}