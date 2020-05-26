package com.greenbee.traveler.data

import com.greenbee.traveler.features.usecases.*

class Interactors(
    val getTripList: GetTripList,
    val getTripDetails: GetTripDetails,
    val addOrUpdateTrip: AddOrUpdateTrip,
    val addOrUpdateCategory: AddOrUpdateCategory,
    val addOrUpdateItem: AddOrUpdateItem,
    val removeTrip: RemoveTrip,
    val removeCategory: RemoveCategory,
    val removeItem: RemoveItem
)
