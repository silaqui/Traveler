package com.greenbee.traveler.features.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.greenbee.traveler.data.toTrip
import com.greenbee.traveler.domain.data.TravelerRepository
import com.greenbee.traveler.domain.entities.Trip

class GetTripDetailLive(private val travelerRepository: TravelerRepository) {

    operator fun invoke(id: String): LiveData<Trip?> {
        val tripDetailLiveData = travelerRepository.getTripDetailLiveData(id)
        return Transformations.map(tripDetailLiveData) { it?.toTrip() }
    }

}