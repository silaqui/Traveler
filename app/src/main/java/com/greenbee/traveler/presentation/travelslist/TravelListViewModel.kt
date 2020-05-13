package com.greenbee.traveler.presentation.travelslist

import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.greenbee.traveler.domain.data.TravelerRepository
import com.greenbee.traveler.domain.entities.Trip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class TravelListViewModel internal constructor(
    val travelerRepository: TravelerRepository
) :
    ViewModel() {

    private val tripListState = MutableLiveData<List<Trip>>()
    val tripList: LiveData<List<Trip>> get() = tripListState

    private val _navigateToTripDetails = MutableLiveData<Long>()
    val navigateToTripDetails: LiveData<Long> get() = _navigateToTripDetails

    var imageView: ImageView? = null
    init {
        runBlocking {
            tripListState.value = travelerRepository.getTrips()
        }
    }

    suspend fun add() {
        withContext(Dispatchers.IO) {
            travelerRepository.addNewTrip(Trip(title = "Title", note = "Lorem Ipsum"))
        }
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
        runBlocking {
            tripListState.value = travelerRepository.getTrips()
        }
    }
}
