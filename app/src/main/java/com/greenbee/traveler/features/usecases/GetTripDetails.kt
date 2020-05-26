package com.greenbee.traveler.features.usecases

import com.greenbee.traveler.domain.data.TravelerRepository
import com.greenbee.traveler.domain.entities.Trip
import com.greenbee.traveler.domain.usecases.UseCase

class GetTripDetails(private val repository: TravelerRepository) :
    UseCase<Trip, GetTripDetails.Params>() {

    override suspend fun run(params: Params) = repository.getTripDetail(params.id)

    data class Params(val id: String)

}