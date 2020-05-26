package com.greenbee.traveler.features.usecases

import com.greenbee.traveler.domain.data.TravelerRepository
import com.greenbee.traveler.domain.usecases.UseCase

class RemoveCategory(private val repository: TravelerRepository) :
    UseCase<Unit, RemoveCategory.Params>() {

    override suspend fun run(params: Params) =
        repository.removeCategory(params.tripId, params.categoryId)

    data class Params(val tripId: String, val categoryId: String)

}