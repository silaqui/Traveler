package com.greenbee.traveler.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.greenbee.traveler.DATABASE_NAME

@Database(
    entities = [TripEntity::class, CategoryEntity::class, ItemEntity::class],
    version = 2,
    exportSchema = false
)
abstract class TravelerRoomDataBase : RoomDatabase() {

    companion object {

        @Volatile
        private var instance: TravelerRoomDataBase? = null

        private fun create(context: Context): TravelerRoomDataBase =
            Room.databaseBuilder(
                context.applicationContext,
                TravelerRoomDataBase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()

        fun getInstance(context: Context): TravelerRoomDataBase =
            synchronized(this) {
                (instance ?: create(context)).also { instance = it }
            }
    }

    abstract fun tripDao(): TripDao

    abstract fun categoryDao(): CategoryDao

    abstract fun itemDao(): ItemDao
}