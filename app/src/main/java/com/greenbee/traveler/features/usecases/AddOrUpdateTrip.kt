package com.greenbee.traveler.features.usecases

import com.greenbee.traveler.domain.data.TravelerRepository
import com.greenbee.traveler.domain.entities.Trip
import com.greenbee.traveler.domain.usecases.UseCase

class AddOrUpdateTrip(private val repository: TravelerRepository) :
    UseCase<String, AddOrUpdateTrip.Params>() {

    override suspend fun run(params: Params) = repository.addOrUpdateTrip(params.trip)

    data class Params(val trip: Trip)

}