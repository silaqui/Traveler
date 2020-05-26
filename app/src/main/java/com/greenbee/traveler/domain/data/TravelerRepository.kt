package com.greenbee.traveler.domain.data

import com.greenbee.traveler.domain.entities.Category
import com.greenbee.traveler.domain.entities.Item
import com.greenbee.traveler.domain.entities.Trip

class TravelerRepository(private val dataSource: TravelerDataSource) {

    suspend fun getTrips() = dataSource.getTrips()
    suspend fun addOrUpdateTrip(trip: Trip) = dataSource.addOrUpdateTrip(trip)
    suspend fun removeTrip(tripId: String) = dataSource.removeTrip(Trip(id = tripId))

    suspend fun getCategory(tripId: String) = dataSource.getCategory(tripId)

    suspend fun addOrUpdateCategory(tripId: String, category: Category) =
        dataSource.addOrUpdateCategory(tripId, category)

    suspend fun removeCategory(tripId: String, categoryId: String) =
        dataSource.removeCategory(tripId, Category(id = categoryId))

    suspend fun getItems(tripId: String, categoryId: String) =
        dataSource.getItems(tripId, categoryId)

    suspend fun addOrUpdateItem(tripId: String, categoryId: String, item: Item) =
        dataSource.addOrUpdateItem(tripId, categoryId, item)

    suspend fun removeItem(tripId: String, categoryId: String, itemId: String) =
        dataSource.removeItem(tripId, categoryId, Item(id = itemId))

    suspend fun getTripDetail(tripId: String) = dataSource.getTripDetail(tripId)

}