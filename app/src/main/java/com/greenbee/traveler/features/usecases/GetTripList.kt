package com.greenbee.traveler.features.usecases

import arrow.core.None
import com.greenbee.traveler.domain.data.TravelerRepository
import com.greenbee.traveler.domain.entities.Trip
import com.greenbee.traveler.domain.usecases.UseCase

class GetTripList(private val repository: TravelerRepository) : UseCase<List<Trip>, None>() {

    override suspend fun run(params: None) = repository.getTrips()

}