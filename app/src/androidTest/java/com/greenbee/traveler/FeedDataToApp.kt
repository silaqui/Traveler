package com.greenbee.traveler

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.greenbee.traveler.data.RoomTravelerDataSource
import com.greenbee.traveler.data.db.TravelerRoomDataBase
import com.greenbee.traveler.domain.data.TravelerRepository
import com.greenbee.traveler.domain.entities.Category
import com.greenbee.traveler.domain.entities.Item
import com.greenbee.traveler.domain.entities.Trip
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FeedDataToApp {

    lateinit var tripId1: String
    lateinit var tripId2: String
    lateinit var tripId3: String
    lateinit var tripId4: String

    lateinit var categoryId11: String
    lateinit var categoryId12: String
    lateinit var categoryId13: String
    lateinit var categoryId14: String
    lateinit var categoryId21: String
    lateinit var categoryId22: String
    lateinit var categoryId23: String

    @Test
    fun feedManualTestData() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val rdb = TravelerRoomDataBase.getInstance(context)
        val rds = RoomTravelerDataSource(rdb)
        val db = TravelerRepository(rds)

        runBlocking {
            rdb.tripDao().clear()

            tripId1 = db.addOrUpdateTrip(Trip(title = "Tygryskowo")).fold({ "ERROR" }, { it })
            tripId2 = db.addOrUpdateTrip(Trip(title = "Hel")).fold({ "ERROR" }, { it })
            tripId3 = db.addOrUpdateTrip(Trip(title = "Wyjazd w góry")).fold({ "ERROR" }, { it })
            tripId4 = db.addOrUpdateTrip(Trip(title = "Egipt")).fold({ "ERROR" }, { it })

            categoryId11 = db.addOrUpdateCategory(
                tripId1,
                category = Category(title = "Ubrania")
            ).fold({ "ERROR" }, { it })
            categoryId12 = db.addOrUpdateCategory(
                tripId1,
                category = Category(title = "Dokumenty")
            ).fold({ "ERROR" }, { it })
            categoryId13 = db.addOrUpdateCategory(
                tripId1,
                category = Category(title = "Jedzenie")
            ).fold({ "ERROR" }, { it })
            categoryId14 = db.addOrUpdateCategory(
                tripId1,
                category = Category(title = "Do spanka")
            ).fold({ "ERROR" }, { it })
            categoryId21 = db.addOrUpdateCategory(
                tripId2,
                category = Category(title = "Ubrania")
            ).fold({ "ERROR" }, { it })
            categoryId22 = db.addOrUpdateCategory(
                tripId2,
                category = Category(title = "Dokumenty")
            ).fold({ "ERROR" }, { it })
            categoryId23 = db.addOrUpdateCategory(
                tripId2,
                category = Category(title = "Jedzenie")
            ).fold({ "ERROR" }, { it })

            db.addOrUpdateItem(
                tripId1,
                categoryId11,
                Item(name = "Majtasy", isDone = true)
            )
            db.addOrUpdateItem(
                tripId1,
                categoryId11,
                Item(name = "Kurtka")
            )
            db.addOrUpdateItem(
                tripId1,
                categoryId11,
                Item(name = "Skarpety")
            )
            db.addOrUpdateItem(
                tripId1,
                categoryId11,
                Item(name = "Bluza")
            )
            db.addOrUpdateItem(
                tripId1,
                categoryId11,
                Item(name = "T-shirt")
            )
            db.addOrUpdateItem(
                tripId1,
                categoryId11,
                Item(name = "Czapka")
            )
            db.addOrUpdateItem(
                tripId1,
                categoryId11,
                Item(name = "But")
            )
            db.addOrUpdateItem(
                tripId1,
                categoryId11,
                Item(name = "Lewy But")
            )
            db.addOrUpdateItem(
                tripId1,
                categoryId11,
                Item(name = "Szal")
            )
            db.addOrUpdateItem(
                tripId1,
                categoryId11,
                Item(name = "Majtasy")
            )

            db.addOrUpdateItem(
                tripId1,
                categoryId12,
                Item(name = "Paszport", isDone = true)
            )
            db.addOrUpdateItem(
                tripId1,
                categoryId12,
                Item(name = "Prawo jazdy")
            )
            db.addOrUpdateItem(
                tripId1,
                categoryId12,
                Item(name = "Dokumanty samochodu")
            )

        }
    }

}