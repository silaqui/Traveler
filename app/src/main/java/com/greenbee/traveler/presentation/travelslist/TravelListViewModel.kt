package com.greenbee.traveler.presentation.travelslist

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.core.None
import com.greenbee.traveler.data.Interactors
import com.greenbee.traveler.domain.entities.Trip
import com.greenbee.traveler.domain.exceptions.Failure
import com.greenbee.traveler.features.usecases.AddOrUpdateTrip
import com.greenbee.traveler.features.usecases.RemoveTrip

class TravelListViewModel internal constructor(
    private val interactors: Interactors
) :
    ViewModel() {

    private val tripListState = MutableLiveData<List<Trip>>()
    val tripList: LiveData<List<Trip>> get() = tripListState

    private val _navigateToTripDetails = MutableLiveData<String>()
    val navigateToTripDetails: LiveData<String> get() = _navigateToTripDetails

    var imageView: ImageView? = null

    init {
        refreshTripList()
    }

    fun refreshTripList() {
        interactors.getTripList(None) { it.fold(::handleFailure, ::handleTripList) }
    }

    private fun handleFailure(failure: Failure) {}

    private fun handleTripList(trips: List<Trip>) {
        tripListState.postValue(trips)
    }

    fun add(title: String) {
        interactors.addOrUpdateTrip(AddOrUpdateTrip.Params(Trip(title = title)))
        { add ->
            add.fold(
                ::handleFailure,
                ::handleAdd
            )
        }
    }

    fun undoDelete(trip: Trip) {
        interactors.addOrUpdateTrip(AddOrUpdateTrip.Params(trip)) {
            it.fold({}, { refreshTripList() })
        }
    }

    private fun handleAdd(id: String) {
        interactors.getTripList(None) { refresh ->
            refresh.fold(
                ::handleFailure
            ) {
                tripListState.postValue(it)
                _navigateToTripDetails.value = id
            }
        }
    }

    fun deleteAtPosition(position: Int, callback: (Trip) -> Unit) {
        tripList.value?.get(position)?.id?.let {
            interactors.removeTrip(RemoveTrip.Params(it)) {
                refreshTripList()
                it.fold({}, {
                    callback(it)
                    refreshTripList()
                })
            }
        }
    }

    fun onTripClicked(id: String, imageView: ImageView) {
        this.imageView = imageView
        _navigateToTripDetails.value = id
    }

    fun onTripNavigated() {
        _navigateToTripDetails.value = null
    }

    fun doneNavigating() {
        _navigateToTripDetails.value = null
    }
}
