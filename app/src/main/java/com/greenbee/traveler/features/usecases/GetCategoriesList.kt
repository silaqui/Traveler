package com.greenbee.traveler.features.usecases

import androidx.lifecycle.LiveData
import com.greenbee.traveler.domain.data.TravelerRepository
import com.greenbee.traveler.domain.entities.Category

class GetCategoriesList(private val repository: TravelerRepository) {

    operator fun invoke(tripId: String): LiveData<List<Category>> = repository.getCategory(tripId)

}