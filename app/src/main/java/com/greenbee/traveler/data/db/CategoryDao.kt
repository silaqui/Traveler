package com.greenbee.traveler.data.db

import androidx.room.*

@Dao
interface CategoryDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(category: CategoryEntity) : Long

    @Update
    suspend fun update(category: CategoryEntity)

    @Delete
    suspend fun delete(category: CategoryEntity)

    @Query("SELECT * FROM items_list_table WHERE tripId = :tripId")
    suspend fun get(tripId: Long): List<CategoryEntity>

    @Query("DELETE FROM items_list_table")
    suspend fun clear()

}