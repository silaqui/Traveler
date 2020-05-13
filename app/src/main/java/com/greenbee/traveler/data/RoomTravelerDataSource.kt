package com.greenbee.traveler.data

import com.greenbee.traveler.data.db.TravelerRoomDataBase
import com.greenbee.traveler.domain.data.TravelerDataSource
import com.greenbee.traveler.domain.entities.Category
import com.greenbee.traveler.domain.entities.Item
import com.greenbee.traveler.domain.entities.Trip

class RoomTravelerDataSource(dataSource: TravelerRoomDataBase) : TravelerDataSource {

    var db = dataSource

    override suspend fun getTrips(): List<Trip> {
        return db.tripDao().getAll().map { it.toTrip }
    }

    override suspend fun addNewTrip(trip: Trip): String {
        val tripRoomEntity = trip.toRoomEntity
        return db.tripDao().add(tripRoomEntity).toString()
    }

    override suspend fun removeTrip(trip: Trip) {
        db.tripDao().delete(trip.toRoomEntity)
    }

    override suspend fun getCategory(trip: Trip): List<Category> {
        return db.categoryDao().get(trip.id).map { it.toCategory }
    }

    override suspend fun addNewCategory(trip: Trip, category: Category): String {
        return db.categoryDao().add(category.toRoomEntity(trip.id)).toString()
    }

    override suspend fun removeCategory(trip: Trip, category: Category) {
        db.categoryDao().delete(category.toRoomEntity(trip.id))
    }

    override suspend fun getItems(trip: Trip, category: Category): List<Item> {
        return db.itemDao().get(category.id).map { it.toItem }
    }

    override suspend fun addOrUpdateItem(trip: Trip, category: Category, item: Item): String {
        return db.itemDao().addOrUpdate(item.toRoomEntity(category.id)).toString()
    }

    override suspend fun removeItem(trip: Trip, category: Category, item: Item) {
        db.itemDao().delete(item.toRoomEntity(category.id))
    }

    override suspend fun getTripDetail(tripId: String): Trip? {
        val tripDetails = db.tripDao().getTripWithIdDetails(tripId.toLong())
        return tripDetails?.toTrip()
    }
}