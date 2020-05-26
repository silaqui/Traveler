package com.greenbee.traveler.domain.data

import arrow.core.Either
import com.greenbee.traveler.domain.entities.Category
import com.greenbee.traveler.domain.entities.Item
import com.greenbee.traveler.domain.entities.Trip
import com.greenbee.traveler.domain.exceptions.Failure

interface TravelerDataSource {

    suspend fun getTrips(): Either<Failure, List<Trip>>
    suspend fun addOrUpdateTrip(trip: Trip): Either<Failure, String>
    suspend fun removeTrip(trip: Trip): Either<Failure, Unit>

    suspend fun getCategory(tripId: String): Either<Failure, List<Category>>
    suspend fun addOrUpdateCategory(tripId: String, category: Category): Either<Failure, String>
    suspend fun removeCategory(tripId: String, category: Category): Either<Failure, Unit>

    suspend fun getItems(tripId: String, categoryId: String): Either<Failure, List<Item>>
    suspend fun addOrUpdateItem(
        tripId: String,
        categoryId: String,
        item: Item
    ): Either<Failure, String>

    suspend fun removeItem(tripId: String, categoryId: String, item: Item): Either<Failure, Unit>

    suspend fun getTripDetail(tripId: String): Either<Failure, Trip>

}