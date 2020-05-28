package com.greenbee.traveler.presentation

import android.content.Context
import com.greenbee.traveler.data.Interactors
import com.greenbee.traveler.presentation.traveldetails.TripDetailsViewModelFactory
import com.greenbee.traveler.presentation.travelslist.TravelListViewModelFactory

object InjectorUtils {

    fun provideTravelListViewModelFactory(context: Context): TravelListViewModelFactory {
        return TravelListViewModelFactory(Interactors.getInstance(context.applicationContext))
    }

    fun provideTripDetailsViewModelFactory(context: Context): TripDetailsViewModelFactory {
        return TripDetailsViewModelFactory(Interactors.getInstance(context.applicationContext))
    }

}
