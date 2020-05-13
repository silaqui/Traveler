package com.greenbee.traveler.domain.data

import com.greenbee.traveler.domain.entities.Category
import com.greenbee.traveler.domain.entities.Item
import com.greenbee.traveler.domain.entities.Trip

class TravelerRepository(private val travelerDataSource: TravelerDataSource) {

    suspend fun getTrips(): List<Trip> = travelerDataSource.getTrips()
    suspend fun addNewTrip(trip: Trip): String = travelerDataSource.addNewTrip(trip)
    suspend fun removeTrip(trip: Trip): Unit = travelerDataSource.removeTrip(trip)

    suspend fun getCategory(trip: Trip): List<Category> =
        travelerDataSource.getCategory(trip)

    suspend fun addNewCategory(trip: Trip, category: Category): String =
        travelerDataSource.addNewCategory(trip, category)

    suspend fun removeCategory(trip: Trip, category: Category): Unit =
        travelerDataSource.removeCategory(trip, category)

    suspend fun getItems(trip: Trip, category: Category): List<Item> =
        travelerDataSource.getItems(trip, category)

    suspend fun addOrUpdateItem(trip: Trip, category: Category, item: Item): String =
        travelerDataSource.addOrUpdateItem(trip, category, item)

    suspend fun removeItem(trip: Trip, category: Category, item: Item): Unit =
        travelerDataSource.removeItem(trip, category, item)

    suspend fun getTripDetail(tripId: String): Trip? =
        travelerDataSource.getTripDetail(tripId)

}