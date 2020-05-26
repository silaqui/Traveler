package com.greenbee.traveler.features.usecases

import com.greenbee.traveler.domain.data.TravelerRepository
import com.greenbee.traveler.domain.entities.Item
import com.greenbee.traveler.domain.usecases.UseCase

class AddOrUpdateItem(private val repository: TravelerRepository) :
    UseCase<String, AddOrUpdateItem.Params>() {

    override suspend fun run(params: Params) = repository.addOrUpdateItem(
        params.tripId,
        params.categoryId, params.item
    )

    data class Params(val tripId: String, val categoryId: String, val item: Item)

}