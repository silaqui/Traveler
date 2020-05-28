package com.greenbee.traveler.presentation.traveldetails

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.greenbee.traveler.data.Interactors
import com.greenbee.traveler.domain.entities.Trip
import java.text.SimpleDateFormat
import java.util.*

class TripDetailsViewModel internal constructor(
    private val interactors: Interactors
) : ViewModel() {

    var cardsVisibility = View.GONE

    private lateinit var tripState: LiveData<Trip?>
    val trip: LiveData<Trip?> get() = tripState

    fun setTripId(id: String) {
        tripState = interactors.getTripDetailsLive(id)
    }
}

@BindingAdapter("formattedData")
fun TextView.formattedData(timeMillis: Long?) {
    timeMillis?.let {
        val format = SimpleDateFormat("dd/MM/yyy")
        text = format.format(Date(timeMillis))
    }

}