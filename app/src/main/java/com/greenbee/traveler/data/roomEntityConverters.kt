package com.greenbee.traveler.data

import com.greenbee.traveler.data.db.*
import com.greenbee.traveler.domain.entities.Category
import com.greenbee.traveler.domain.entities.Item
import com.greenbee.traveler.domain.entities.Trip

internal val Trip.toRoomEntity: TripEntity
    get() = if (id.toLong() == -1L) {
        TripEntity(
            title = title,
            note = note,
            backgroundUrl = backgroundUrl,
            date = date
        )
    } else {
        TripEntity(
            id.toLong(),
            title,
            note,
            backgroundUrl,
            date
        )
    }

internal val TripEntity.toTrip: Trip
    get() = Trip(id.toString(), title, note, backgroundUrl, date)

fun Category.toRoomEntity(tripId: String): CategoryEntity {
    return if (id.toLong() == -1L) {
        CategoryEntity(
            tripId = tripId.toLong(),
            title = title
        )
    } else {
        CategoryEntity(id.toLong(), tripId.toLong(), title)
    }
}

internal val CategoryEntity.toCategory: Category
    get() = Category(id.toString(), title)

fun Item.toRoomEntity(categoryId: Long): ItemEntity {
    return if (id.toLong() == -1L) {
        ItemEntity(
            categoryId = categoryId,
            name = name,
            isDone = isDone
        )
    } else {
        ItemEntity(id.toLong(), categoryId, name, isDone)
    }
}

internal val ItemEntity.toItem: Item
    get() = Item(id.toString(), name, isDone)

fun CategoryWithItems.toCategories(): Category {
    val output = category.toCategory
    val items = items.map { it.toItem }
    output.items.addAll(items)
    return output
}

fun TripDetails.toTrip(): Trip {
    val output = trip.toTrip
    val categories = categories.map { it.toCategories() }
    output.categories.addAll(categories)
    return output
}