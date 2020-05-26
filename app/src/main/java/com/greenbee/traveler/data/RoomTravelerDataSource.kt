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
        val tripId = db.tripDao().addOrUpdate(trip.toRoomEntity).toString()
        val categories = trip.categories

        for (category in categories) {
            addOrUpdateCategory(tripId, category)
        }

        return Either.Right(tripId)
    }

    override suspend fun removeTrip(trip: Trip): Either<Failure, Unit> {
        db.tripDao().delete(trip.toRoomEntity)
        return Either.Right(Unit)
    }

    override suspend fun getCategory(tripId: String): Either<Nothing, List<Category>> {
        return Either.Right(db.categoryDao().get(tripId.toLong()).map { it.toCategory })
    }

    override suspend fun addOrUpdateCategory(
        tripId: String,
        category: Category
    ): Either<Nothing, String> {
        val categoryId = db.categoryDao().addOrUpdate(category.toRoomEntity(tripId)).toString()
        val items = category.items

        for (item in items) {
            addOrUpdateItem(tripId, categoryId, item)
        }

        return Either.Right(categoryId)
    }

    override suspend fun removeCategory(tripId: String, category: Category): Either<Failure, Unit> {
        db.categoryDao().delete(category.toRoomEntity(tripId))
        return Either.Right(Unit)
    }

    override suspend fun getItems(tripId: String, categoryId: String): Either<Nothing, List<Item>> {
        return Either.Right(db.itemDao().get(categoryId.toLong()).map { it.toItem })
    }

    override suspend fun addOrUpdateItem(
        tripId: String,
        categoryId: String,
        item: Item
    ): Either<Nothing, String> {
        return Either.Right(
            db.itemDao().addOrUpdate(item.toRoomEntity(categoryId.toLong())).toString()
        )
    }

    override suspend fun removeItem(
        tripId: String,
        categoryId: String,
        item: Item
    ): Either<Nothing, Unit> {
        db.itemDao().delete(item.toRoomEntity(categoryId.toLong()))
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