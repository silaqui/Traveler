package com.greenbee.traveler.features.usecases

import androidx.lifecycle.LiveData
import com.greenbee.traveler.domain.data.TravelerRepository
import com.greenbee.traveler.domain.entities.Item

class GetItemsList(private val repository: TravelerRepository) {

    operator fun invoke(tripId: String, categoryId: String): LiveData<List<Item>> =
        repository.getItems(tripId, categoryId)

}