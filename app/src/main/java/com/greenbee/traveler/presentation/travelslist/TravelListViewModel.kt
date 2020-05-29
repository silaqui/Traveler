package com.greenbee.traveler.presentation.travelslist

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.greenbee.traveler.data.Interactors
import com.greenbee.traveler.domain.entities.Trip
import com.greenbee.traveler.domain.exceptions.Failure
import com.greenbee.traveler.features.usecases.AddOrUpdateTrip
import com.greenbee.traveler.features.usecases.RemoveTrip

class TravelListViewModel internal constructor(
    private val interactors: Interactors
) :
    ViewModel() {

    private var tripListState = interactors.getTripsList()
    val tripList: LiveData<List<Trip>> get() = tripListState

    private val _navigateToTripDetails = MutableLiveData<String>()
    val navigateToTripDetails: LiveData<String> get() = _navigateToTripDetails

    var imageView: ImageView? = null
    var backgroundUrl: String? = null

    private fun handleFailure(failure: Failure) {}

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
        interactors.addOrUpdateTrip(AddOrUpdateTrip.Params(trip)) { }
    }

    private fun handleAdd(id: String) {
        _navigateToTripDetails.value = id
    }

    fun deleteAtPosition(position: Int, callback: (Trip) -> Unit) {
        tripList.value?.get(position)?.id?.let {
            interactors.removeTrip(RemoveTrip.Params(it)) {
                it.fold({}, { callback(it) })
            }
        }
    }

    fun onTripClicked(id: String, imageView: ImageView, backgroundUrl: String) {
        this.imageView = imageView
        this.backgroundUrl = backgroundUrl
        _navigateToTripDetails.value = id
    }

    fun onTripNavigated() {
        imageView = null
        backgroundUrl = null
        _navigateToTripDetails.value = null
    }
}
