package com.greenbee.traveler.data.db

import androidx.room.*

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdate(item: ItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdate(item: List<ItemEntity>)

    @Update
    suspend fun update(item: ItemEntity)

    @Delete
    suspend fun delete(item: ItemEntity)

    @Query("SELECT * FROM item_table WHERE categoryId = :categoryId")
    suspend fun get(categoryId: Long): List<ItemEntity>

    @Query("DELETE FROM item_table")
    suspend fun clear()


}