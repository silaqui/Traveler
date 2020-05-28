package com.greenbee.traveler.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CategoryDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdate(category: CategoryEntity): Long

    @Delete
    suspend fun delete(category: CategoryEntity)

    @Query("SELECT * FROM items_list_table WHERE tripId = :tripId")
    suspend fun get(tripId: Long): List<CategoryEntity>

    @Query("SELECT * FROM items_list_table WHERE tripId = :tripId")
    fun getLiveData(tripId: Long): LiveData<List<CategoryEntity>>

    @Query("DELETE FROM items_list_table")
    suspend fun clear()

}