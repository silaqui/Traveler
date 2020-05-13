package com.greenbee.traveler.domain.data

import com.greenbee.traveler.domain.entities.Item
import com.greenbee.traveler.domain.entities.Category
import com.greenbee.traveler.domain.entities.Trip

interface TravelerDataSource {

    suspend fun getTrips(): List<Trip>
    suspend fun addNewTrip(trip: Trip): String
    suspend fun removeTrip(trip: Trip): Unit

    suspend fun getCategory(trip: Trip): List<Category>
    suspend fun addNewCategory(trip: Trip, category: Category): String
    suspend fun removeCategory(trip: Trip, category: Category): Unit

    suspend fun getItems(trip: Trip, category: Category): List<Item>
    suspend fun addOrUpdateItem(trip: Trip, category: Category, item: Item): String
    suspend fun removeItem(trip: Trip, category: Category, item: Item): Unit

    suspend fun getTripDetail(tripId: String): Trip?

}