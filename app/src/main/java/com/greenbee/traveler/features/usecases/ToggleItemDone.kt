package com.greenbee.traveler.features.usecases

import com.greenbee.traveler.domain.data.TravelerRepository
import com.greenbee.traveler.domain.entities.Item
import com.greenbee.traveler.domain.usecases.UseCase

class ToggleItemDone(private val repository: TravelerRepository) :
    UseCase<String, ToggleItemDone.Params>() {

    override suspend fun run(params: Params) = repository.addOrUpdateItem(
        tripId = params.tripId,
        categoryId = params.categoryId,
        item = params.item.copy(isDone = !params.item.isDone)
    )

    data class Params(val tripId: String, val categoryId: String, val item: Item)

}