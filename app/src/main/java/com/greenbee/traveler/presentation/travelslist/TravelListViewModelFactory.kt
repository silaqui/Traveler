package com.greenbee.traveler.presentation.travelslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.greenbee.traveler.domain.data.TravelerRepository

class TravelListViewModelFactory(private val travelerRepository: TravelerRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TravelListViewModel(travelerRepository) as T
    }
}