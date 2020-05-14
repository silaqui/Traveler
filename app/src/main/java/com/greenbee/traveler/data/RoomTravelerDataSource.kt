package com.greenbee.traveler.data

import arrow.core.Either
import com.greenbee.traveler.data.db.TravelerRoomDataBase
import com.greenbee.traveler.domain.data.TravelerDataSource
import com.greenbee.traveler.domain.entities.Category
import com.greenbee.traveler.domain.entities.Item
import com.greenbee.traveler.domain.entities.Trip
import com.greenbee.traveler.domain.exceptions.Failure

class RoomTravelerDataSource(dataSource: TravelerRoomDataBase) : TravelerDataSource {

    var db = dataSource

    override suspend fun getTrips(): Either<Failure, List<Trip>> {
        return Either.Right(db.tripDao().getAll().map { it.toTrip })
    }

    override suspend fun addOrUpdateTrip(trip: Trip): Either<Failure, String> {
        val tripRoomEntity = trip.toRoomEntity
        return Either.Right(db.tripDao().addOrUpdate(tripRoomEntity).toString())
    }

    override suspend fun removeTrip(trip: Trip): Either<Failure, Unit> {
        db.tripDao().delete(trip.toRoomEntity)
        return Either.Right(Unit)
    }

    override suspend fun getCategory(trip: Trip): Either<Nothing, List<Category>> {
        return Either.Right(db.categoryDao().get(trip.id).map { it.toCategory })
    }

    override suspend fun addOrUpdateCategory(
        trip: Trip,
        category: Category
    ): Either<Nothing, String> {
        return Either.Right(db.categoryDao().addOrUpdate(category.toRoomEntity(trip.id)).toString())
    }

    override suspend fun removeCategory(trip: Trip, category: Category): Either<Failure, Unit> {
        db.categoryDao().delete(category.toRoomEntity(trip.id))
        return Either.Right(Unit)
    }

    override suspend fun getItems(trip: Trip, category: Category): Either<Nothing, List<Item>> {
        return Either.Right(db.itemDao().get(category.id).map { it.toItem })
    }

    override suspend fun addOrUpdateItem(
        trip: Trip,
        category: Category,
        item: Item
    ): Either<Nothing, String> {
        return Either.Right(db.itemDao().addOrUpdate(item.toRoomEntity(category.id)).toString())
    }

    override suspend fun removeItem(
        trip: Trip,
        category: Category,
        item: Item
    ): Either<Nothing, Unit> {
        db.itemDao().delete(item.toRoomEntity(category.id))
        return Either.Right(Unit)
    }

    override suspend fun getTripDetail(tripId: String): Either<Failure, Trip> {
        val tripDetails = db.tripDao().getTripWithIdDetails(tripId.toLong())
        tripDetails?.let {
            return Either.Right(tripDetails.toTrip())
        }
        return Either.Left(Failure.TripNotFount)
    }
}