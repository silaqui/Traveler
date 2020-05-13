package com.greenbee.traveler.data.db

import androidx.room.Embedded
import androidx.room.Relation

data class TripDetails(
    @Embedded val trip : TripEntity,
    @Relation(
        entity = CategoryEntity::class,
        parentColumn = "id",
        entityColumn = "tripId"
    )
    val categories : List<CategoryWithItems>
)

data class CategoryWithItems(
    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val items : List<ItemEntity>
)