package com.greenbee.traveler.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.greenbee.traveler.data.db.TravelerRoomDataBase
import com.greenbee.traveler.data.db.TripDao
import com.greenbee.traveler.domain.entities.Category
import com.greenbee.traveler.domain.entities.Item
import com.greenbee.traveler.domain.entities.Trip
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RoomTravelerDataSourceTest {

    private lateinit var tested: RoomTravelerDataSource

    private val tripOne = Trip(title = "Tygryskowo")
    private val tripTwo = Trip(title = "ContePartiro")
    private val categoryOne = Category(title = "Category 1")
    private val categoryTwo = Category(title = "Category 2")
    private val itemOne = Item(name = "Item 1")
    private val itemTwo = Item(name = "Item 2")


    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        tested = RoomTravelerDataSource(
            Room.inMemoryDatabaseBuilder(
                context, TravelerRoomDataBase::class.java
            ).build()
        )
    }

    @Test
    fun shouldGetTripsEmptyList() {
        runBlocking {
            //given
            //when
            val actual = tested.getTrips()
            //then
            assertNotNull(actual)
            assertEquals(actual.size, 0)
        }
    }

    @Test
    fun shouldAddNewTrip() {
        runBlocking {
            //given
            //when
            tested.addNewTrip(tripOne)
            tested.addNewTrip(tripTwo)
            val actual = tested.getTrips()
            //then
            assertNotNull(actual)
            assertThat(actual.size, equalTo(2))
        }
    }

    @Test
    fun shouldRemoveTrip() {
        runBlocking {
            //given
            val tripOneId = tested.addNewTrip(tripOne)
            tested.addNewTrip(tripTwo)
            tested.addNewCategory(Trip(1), categoryOne)
            tested.addNewCategory(Trip(1), categoryTwo)
            tested.addOrUpdateItem(tripOne, Category(1), itemOne)
            tested.addOrUpdateItem(tripOne, Category(1), itemTwo)
            val toRemove = tested.getTrips().find { it.id == tripOneId.toLong() }!!
            val categories = tested.getCategory(toRemove)
            //when
            tested.removeTrip(toRemove)
            val actual = tested.getTrips()
            //then
            val cleanedCategories = tested.getCategory(toRemove)
            val cleanedItems = ArrayList<Item>()
            for (category in categories) {
                cleanedItems.addAll(tested.getItems(toRemove,category))
            }
            assertNotNull(actual)
            assertThat(actual.size, equalTo(1))
            assertThat(actual[0].title, equalTo(tripTwo.title))
            assertThat(cleanedCategories.size, equalTo(0))
            assertThat(cleanedItems.size, equalTo(0))
        }
    }

    @Test
    fun shouldInsertCategory() {
        runBlocking {
            //given
            tested.addNewTrip(tripOne)
            val trip = tested.getTrips()[0]
            //when
            tested.addNewCategory(trip, categoryOne)
            tested.addNewCategory(trip, categoryTwo)
            val actual = tested.getCategory(trip)
            //then
            assertNotNull(actual)
            assertThat(actual.size, equalTo(2))
            assertThat(actual[0].title, equalTo(categoryOne.title))
        }
    }

    @Test
    fun shouldRemoveCategory() {
        runBlocking {
            //given
            tested.addNewTrip(tripOne)
            val trip = tested.getTrips()[0]
            val categoryOneId = tested.addNewCategory(trip, categoryOne)
            tested.addNewCategory(trip, categoryTwo)
            tested.addOrUpdateItem(tripOne, Category(1), itemOne)
            tested.addOrUpdateItem(tripOne, Category(1), itemTwo)
            val toRemove = tested.getCategory(trip).find { it.id == categoryOneId.toLong() }
            //when
            tested.removeCategory(trip, toRemove!!)
            val actual = tested.getCategory(trip)
            //then
            val cleanedItems = tested.getItems(trip,toRemove)
            assertNotNull(actual)
            assertThat(actual.size, equalTo(1))
            assertThat(actual[0].title, equalTo(categoryTwo.title))
            assertThat(cleanedItems.size, equalTo(0))
        }
    }

    @Test
    fun shouldAddItem() {
        runBlocking {
            //given
            val tripId = tested.addNewTrip(tripOne).toLong()
            val categoryIdOne = tested.addNewCategory(Trip(tripId), categoryOne).toLong()
            val categoryIdTwo = tested.addNewCategory(Trip(tripId), categoryTwo).toLong()
            //when
            tested.addOrUpdateItem(Trip(tripId), Category(categoryIdOne), itemOne)
            tested.addOrUpdateItem(Trip(tripId), Category(categoryIdOne), itemTwo)
            tested.addOrUpdateItem(Trip(tripId), Category(categoryIdTwo), itemTwo)
            val actual = tested.getItems(Trip(tripId), Category(categoryIdOne))
            //then
            assertNotNull(actual)
            assertThat(actual.size, equalTo(2))
            assertThat(actual[0].name, equalTo(itemOne.name))
            assertThat(actual[0].isDone, equalTo(false))
        }
    }

    @Test
    fun shouldUpdateItem() {
        runBlocking {
            //given
            tested.addNewTrip(tripOne)
            tested.addNewCategory(Trip(1), categoryOne)
            tested.addNewCategory(Trip(1), categoryTwo)
            //when
            val itemOneId = tested.addOrUpdateItem(Trip(1), Category(1), itemOne)
            tested.addOrUpdateItem(Trip(1), Category(1), itemTwo)
            tested.addOrUpdateItem(Trip(1), Category(2), itemTwo)
            tested.addOrUpdateItem(
                tripOne,
                Category(1),
                Item(itemOneId.toLong(), itemOne.name, true)
            )
            val actual = tested.getItems(Trip(1), Category(1))
            //then
            assertNotNull(actual)
            assertThat(actual.size, equalTo(2))
            assertThat(actual[0].name, equalTo(itemOne.name))
            assertThat(actual[0].isDone, equalTo(true))
        }
    }

    @Test
    fun shouldRemoveItem() {
        runBlocking {
            //given
            tested.addNewTrip(tripOne)
            tested.addNewCategory(Trip(1), categoryOne)
            val itemOneId = tested.addOrUpdateItem(Trip(1), Category(1), itemOne)
            tested.addOrUpdateItem(Trip(1), Category(1), itemTwo)
            val toRemove =
                tested.getItems(Trip(1), Category(1)).find { it.id == itemOneId.toLong() }
            //when
            tested.removeItem(Trip(1), Category(1), toRemove!!)
            val actual = tested.getItems(Trip(1), Category(1))
            //then
            assertNotNull(actual)
            assertThat(actual.size, equalTo(1))
            assertThat(actual[0].name, equalTo(itemTwo.name))
        }
    }

    @Test
    fun shouldGetTripDetails() {
        runBlocking {
            //given
            tested.addNewTrip(tripOne)
            tested.addNewCategory(Trip(1), categoryOne)
            tested.addNewCategory(Trip(1), categoryTwo)
            tested.addOrUpdateItem(tripOne, Category(1), itemOne)
            tested.addOrUpdateItem(tripOne, Category(1), itemTwo)
            //when
            val actual = tested.getTripDetail(1.toString())
            //then
            assertNotNull(actual)
            assertThat(actual!!.title, equalTo(tripOne.title))
            assertThat(actual.categories.size, equalTo(2))
            assertThat(actual.categories[0].title, equalTo(categoryOne.title))
            assertThat(actual.categories[1].title, equalTo(categoryTwo.title))
            assertThat(actual.categories[0].items.size, equalTo(2))
            assertThat(actual.categories[0].items[0].name, equalTo(itemOne.name))
            assertThat(actual.categories[0].items[1].name, equalTo(itemTwo.name))
        }
    }

}

