package com.greenbee.traveler.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "items_list_table",
    foreignKeys =
    [ForeignKey(
        onDelete = CASCADE, entity = TripEntity::class,
        parentColumns = ["id"], childColumns = ["tripId"]
    )]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tripId: Long,
    val title: String
)