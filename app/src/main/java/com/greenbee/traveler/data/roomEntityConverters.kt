package com.greenbee.traveler.data

import com.greenbee.traveler.data.db.*
import com.greenbee.traveler.domain.entities.Category
import com.greenbee.traveler.domain.entities.Item
import com.greenbee.traveler.domain.entities.Trip

internal val Trip.toRoomEntity: TripEntity
    get() = if (id == -1L) {
        TripEntity(
            title = title,
            note = note,
            backgroundUrl = backgroundUrl,
            date = date
        )
    } else {
        TripEntity(
            id,
            title,
            note,
            backgroundUrl,
            date
        )
    }

internal val TripEntity.toTrip: Trip
    get() = Trip(id, title, note, backgroundUrl, date)

fun Category.toRoomEntity(tripId: Long): CategoryEntity {
    return if (id == -1L) {
        CategoryEntity(
            tripId = tripId,
            title = title
        )
    } else {
        CategoryEntity(id, tripId, title)
    }
}

internal val CategoryEntity.toCategory: Category
    get() = Category(id, title)

fun Item.toRoomEntity(categoryId: Long): ItemEntity {
    return if (id == -1L) {
        ItemEntity(
            categoryId = categoryId,
            name = name,
            isDone = isDone
        )
    } else {
        ItemEntity(id, categoryId, name, isDone)
    }
}

internal val ItemEntity.toItem: Item
    get() = Item(id, name, isDone)

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