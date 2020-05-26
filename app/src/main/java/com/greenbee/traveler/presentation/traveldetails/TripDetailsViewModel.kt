package com.greenbee.traveler.presentation.traveldetails

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.greenbee.traveler.data.Interactors
import com.greenbee.traveler.domain.entities.Trip
import com.greenbee.traveler.domain.exceptions.Failure
import com.greenbee.traveler.features.usecases.GetTripDetails.Params
import java.text.SimpleDateFormat
import java.util.*

class TripDetailsViewModel internal constructor(
    private val interactors: Interactors
) : ViewModel() {

    var cardsVisibility = View.GONE

    private val tripState = MutableLiveData<Trip>()
    val trip: LiveData<Trip> get() = tripState

    fun setTripId(id: String) {
        interactors.getTripDetails(Params(id)) { it.fold(::handleFailure, ::handleTrip) }
    }

    private fun handleFailure(failure: Failure) {}

    private fun handleTrip(trip: Trip) {
        tripState.postValue(trip)
    }
}

@BindingAdapter("formattedData")
fun TextView.formattedData(timeMillis: Long?) {
    timeMillis?.let {
        val format = SimpleDateFormat("dd/MM/yyy")
        text = format.format(Date(timeMillis))
    }

}