package com.greenbee.traveler.data

import com.greenbee.traveler.domain.usecases.GetTripDetails
import com.greenbee.traveler.domain.usecases.GetTripList

class Interactors(
    val getTripList: GetTripList,
    val getTripDetails: GetTripDetails
)
