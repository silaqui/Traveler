package com.greenbee.traveler.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "item_table",
    foreignKeys =
    [ForeignKey(
        onDelete = ForeignKey.CASCADE, entity = CategoryEntity::class,
        parentColumns = ["id"], childColumns = ["categoryId"]
    )]
)
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val categoryId: Long,
    val name: String,
    val isDone: Boolean
)