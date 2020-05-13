package com.greenbee.traveler.presentation

import android.content.Context
import com.greenbee.traveler.data.RoomTravelerDataSource
import com.greenbee.traveler.data.db.TravelerRoomDataBase
import com.greenbee.traveler.domain.data.TravelerDataSource
import com.greenbee.traveler.domain.data.TravelerRepository
import com.greenbee.traveler.presentation.traveldetails.TripDetailsViewModelFactory
import com.greenbee.traveler.presentation.travelslist.TravelListViewModelFactory

object InjectorUtils {


    fun provideTravelListViewModelFactory(context: Context): TravelListViewModelFactory {
        return TravelListViewModelFactory(getTravelerRepository(context))
    }

    fun provideTripDetailsViewModelFactory(context: Context): TripDetailsViewModelFactory {
        return TripDetailsViewModelFactory(getTravelerRepository(context))
    }

    private fun getTravelerRepository(context: Context): TravelerRepository {
        val travelerRoomDataBase: TravelerRoomDataBase = TravelerRoomDataBase.getInstance(context)
        val travelerDataSource: TravelerDataSource = RoomTravelerDataSource(travelerRoomDataBase)
        return TravelerRepository(travelerDataSource)
    }
}