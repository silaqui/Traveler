package com.greenbee.traveler.presentation.traveldetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.greenbee.traveler.data.Interactors

class TripDetailsViewModelFactory(private val interactors: Interactors) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TripDetailsViewModel(interactors) as T
    }
}
