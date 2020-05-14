package com.greenbee.traveler.domain.usecases

import arrow.core.None
import com.greenbee.traveler.domain.data.TravelerRepository
import com.greenbee.traveler.domain.entities.Trip

class GetTripList(private val repository: TravelerRepository) : UseCase<List<Trip>, None>() {

    override suspend fun run(params: None) = repository.getTrips()

}