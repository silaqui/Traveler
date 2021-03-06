package com.greenbee.traveler.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TripDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrUpdate(trip: TripEntity): Long

    @Delete
    suspend fun delete(trip: TripEntity)

    @Query("SELECT * FROM trip_table ORDER BY id DESC")
    suspend fun getAll(): List<TripEntity>

    @Query("SELECT * FROM trip_table ORDER BY id DESC")
    fun getAllLiveData(): LiveData<List<TripEntity>>

    @Query("DELETE FROM trip_table")
    suspend fun clear()

    @Query("SELECT * from trip_table WHERE id = :id")
    suspend fun getTripWithId(id: Long): TripEntity?

    @Transaction
    @Query("SELECT * FROM trip_table  WHERE id = :id ")
    suspend fun getTripWithIdDetails(id: Long): TripDetails?

    @Transaction
    @Query("SELECT * FROM trip_table  WHERE id = :id ")
    fun getTripWithIdDetailsLiveData(id: Long): LiveData<TripDetails?>
}