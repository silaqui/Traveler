package com.greenbee.traveler.features.usecases

import com.greenbee.traveler.domain.data.TravelerRepository
import com.greenbee.traveler.domain.usecases.UseCase

class RemoveItem(private val repository: TravelerRepository) :
    UseCase<Unit, RemoveItem.Params>() {

    override suspend fun run(params: Params) = repository.removeItem(
        params.tripId,
        params.categoryId,
        params.itemId
    )

    data class Params(val tripId: String, val categoryId: String, val itemId: String)

}