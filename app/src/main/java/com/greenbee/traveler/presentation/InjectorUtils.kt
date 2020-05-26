package com.greenbee.traveler.presentation

import android.content.Context
import com.greenbee.traveler.data.Interactors
import com.greenbee.traveler.data.RoomTravelerDataSource
import com.greenbee.traveler.data.db.TravelerRoomDataBase
import com.greenbee.traveler.domain.data.TravelerDataSource
import com.greenbee.traveler.domain.data.TravelerRepository
import com.greenbee.traveler.features.usecases.*
import com.greenbee.traveler.presentation.traveldetails.TripDetailsViewModelFactory
import com.greenbee.traveler.presentation.travelslist.TravelListViewModelFactory

object InjectorUtils {

    fun provideTravelListViewModelFactory(context: Context): TravelListViewModelFactory {
        return TravelListViewModelFactory(getInteractors(context.applicationContext))
    }

    fun provideTripDetailsViewModelFactory(context: Context): TripDetailsViewModelFactory {
        return TripDetailsViewModelFactory(getInteractors(context.applicationContext))
    }

    private fun getInteractors(context: Context): Interactors {
        val travelerRoomDataBase: TravelerRoomDataBase =
            TravelerRoomDataBase.getInstance(context.applicationContext)
        val travelerDataSource: TravelerDataSource = RoomTravelerDataSource(travelerRoomDataBase)
        val travelerRepository = TravelerRepository(travelerDataSource)

        return Interactors(
            GetTripList(travelerRepository),
            GetTripDetails(travelerRepository),
            AddOrUpdateTrip(travelerRepository),
            AddOrUpdateCategory(travelerRepository),
            AddOrUpdateItem(travelerRepository),
            RemoveTrip(travelerRepository),
            RemoveCategory(travelerRepository),
            RemoveItem(travelerRepository)
        )
    }
}