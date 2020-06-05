package com.greenbee.traveler.data

import android.content.Context
import com.greenbee.traveler.data.db.TravelerRoomDataBase
import com.greenbee.traveler.domain.data.TravelerDataSource
import com.greenbee.traveler.domain.data.TravelerRepository
import com.greenbee.traveler.features.usecases.*

class Interactors(
    val getTripsList: GetTripsList,
    val getCategoriesList: GetCategoriesList,
    val getItemsList: GetItemsList,
    val getTripDetailsLive: GetTripDetailLive,
    val addOrUpdateTrip: AddOrUpdateTrip,
    val addOrUpdateCategory: AddOrUpdateCategory,
    val addOrUpdateItem: AddOrUpdateItem,
    val removeTrip: RemoveTrip,
    val removeCategory: RemoveCategory,
    val removeItem: RemoveItem,
    val toggleItemDone: ToggleItemDone
) {
    companion object {

        @Volatile
        private var instance: Interactors? = null

        private fun create(context: Context): Interactors {
            val travelerRoomDataBase: TravelerRoomDataBase =
                TravelerRoomDataBase.getInstance(context.applicationContext)
            val travelerDataSource: TravelerDataSource =
                RoomTravelerDataSource(travelerRoomDataBase)
            val travelerRepository = TravelerRepository(travelerDataSource)
            return Interactors(
                GetTripsList(travelerRepository),
                GetCategoriesList(travelerRepository),
                GetItemsList(travelerRepository),
                GetTripDetailLive(travelerRepository),
                AddOrUpdateTrip(travelerRepository),
                AddOrUpdateCategory(travelerRepository),
                AddOrUpdateItem(travelerRepository),
                RemoveTrip(travelerRepository),
                RemoveCategory(travelerRepository),
                RemoveItem(travelerRepository),
                ToggleItemDone(travelerRepository)
            )
        }

        fun getInstance(context: Context): Interactors =
            synchronized(this) {
                (instance ?: create(context)).also { instance = it }
            }
    }
}
