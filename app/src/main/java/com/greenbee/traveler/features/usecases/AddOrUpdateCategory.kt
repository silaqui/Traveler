package com.greenbee.traveler.features.usecases

import com.greenbee.traveler.domain.data.TravelerRepository
import com.greenbee.traveler.domain.entities.Category
import com.greenbee.traveler.domain.usecases.UseCase

class AddOrUpdateCategory(private val repository: TravelerRepository) :
    UseCase<String, AddOrUpdateCategory.Params>() {

    override suspend fun run(params: Params) = repository.addOrUpdateCategory(
        params.tripId,
        params.category
    )

    data class Params(val tripId: String, val category: Category)

}