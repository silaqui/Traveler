package com.greenbee.traveler.presentation.travelslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.greenbee.traveler.data.Interactors

class TravelListViewModelFactory(private val interactors: Interactors) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TravelListViewModel(interactors) as T
    }
}