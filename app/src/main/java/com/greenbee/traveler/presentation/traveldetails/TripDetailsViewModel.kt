package com.greenbee.traveler.presentation.traveldetails

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.greenbee.traveler.domain.data.TravelerRepository
import com.greenbee.traveler.domain.entities.Trip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class TripDetailsViewModel internal constructor(
    private val travelerRepository: TravelerRepository
) : ViewModel() {

    var cardsVisibility = View.GONE

    private val tripState = MutableLiveData<Trip>()
    val trip: LiveData<Trip> get() = tripState

    suspend fun setTripId(id: Long) {
        withContext(Dispatchers.IO) {
            val tripDetail = travelerRepository.getTripDetail(id.toString())
            tripDetail?.let {
                tripState.postValue(it)
            }
        }
    }
}

@BindingAdapter("formattedData")
fun TextView.formattedData(timeMillis: Long?) {
    timeMillis?.let {
        val format = SimpleDateFormat("dd/MM/yyy")
        text = format.format(Date(timeMillis))
    }
}