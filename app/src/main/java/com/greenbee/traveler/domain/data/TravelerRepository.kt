package com.greenbee.traveler.domain.data

import com.greenbee.traveler.domain.entities.Category
import com.greenbee.traveler.domain.entities.Item
import com.greenbee.traveler.domain.entities.Trip

class TravelerRepository(private val dataSource: TravelerDataSource) {

    suspend fun getTrips() = dataSource.getTrips()
    suspend fun addOrUpdateTrip(trip: Trip) = dataSource.addOrUpdateTrip(trip)
    suspend fun removeTrip(trip: Trip) = dataSource.removeTrip(trip)

    suspend fun getCategory(trip: Trip) = dataSource.getCategory(trip)

    suspend fun addOrUpdateCategory(trip: Trip, category: Category) =
        dataSource.addOrUpdateCategory(trip, category)

    suspend fun removeCategory(trip: Trip, category: Category) =
        dataSource.removeCategory(trip, category)

    suspend fun getItems(trip: Trip, category: Category) = dataSource.getItems(trip, category)

    suspend fun addOrUpdateItem(trip: Trip, category: Category, item: Item) =
        dataSource.addOrUpdateItem(trip, category, item)

    suspend fun removeItem(trip: Trip, category: Category, item: Item) =
        dataSource.removeItem(trip, category, item)

    suspend fun getTripDetail(tripId: String) = dataSource.getTripDetail(tripId)

}