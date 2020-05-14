package com.greenbee.traveler.presentation.travelslist

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.core.None
import com.greenbee.traveler.data.Interactors
import com.greenbee.traveler.domain.entities.Trip
import com.greenbee.traveler.domain.exceptions.Failure

class TravelListViewModel internal constructor(
    private val interactors: Interactors
) :
    ViewModel() {

    private val tripListState = MutableLiveData<List<Trip>>()
    val tripList: LiveData<List<Trip>> get() = tripListState

    private val _navigateToTripDetails = MutableLiveData<Long>()
    val navigateToTripDetails: LiveData<Long> get() = _navigateToTripDetails

    var imageView: ImageView? = null

    init {
        interactors.getTripList(None) { it.fold(::handleFailure, ::handleTripList) }
    }

    private fun handleFailure(failure: Failure) {}

    private fun handleTripList(trips: List<Trip>) {
        tripListState.postValue(trips)
    }


    fun add() {
        //TODO
    }


    fun onTripClicked(id: Long, imageView: ImageView) {
        this.imageView = imageView
        _navigateToTripDetails.value = id
    }

    fun onTripNavigated() {
        _navigateToTripDetails.value = null
    }

    fun doneNavigating() {
        _navigateToTripDetails.value = null
    }

    fun refresh() {
        interactors.getTripList(None) { it.fold(::handleFailure, ::handleTripList) }
    }
}
