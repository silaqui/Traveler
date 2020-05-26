package com.greenbee.traveler.features.usecases

import arrow.core.Either
import com.greenbee.traveler.domain.data.TravelerRepository
import com.greenbee.traveler.domain.entities.Trip
import com.greenbee.traveler.domain.exceptions.Failure
import com.greenbee.traveler.domain.usecases.UseCase

class RemoveTrip(private val repository: TravelerRepository) :
    UseCase<Trip, RemoveTrip.Params>() {

    override suspend fun run(params: Params): Either<Failure, Trip> {
        val backUp = repository.getTripDetail(params.tripId)
        if (backUp.isLeft()) return backUp
        val removeTrip = repository.removeTrip(params.tripId)
        removeTrip.fold({
            return Either.left(it)
        }, {
            return backUp
        })

    }

    data class Params(val tripId: String)

}