package com.greenbee.traveler.features.usecases

import androidx.lifecycle.LiveData
import com.greenbee.traveler.domain.data.TravelerRepository
import com.greenbee.traveler.domain.entities.Trip

class GetTripsList(private val repository: TravelerRepository) {

    operator fun invoke(): LiveData<List<Trip>> = repository.getTrips()

}