package com.greenbee.traveler.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.greenbee.traveler.data.db.TravelerRoomDataBase
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
            val actual = tested.getTrips().fold({ null }, { it })
            //then
            assertNotNull(actual)
            assertEquals(actual!!.size, 0)
        }
    }

    @Test
    fun shouldAddNewTrip() {
        runBlocking {
            //given
            //when
            tested.addOrUpdateTrip(tripOne)
            tested.addOrUpdateTrip(tripTwo)
            val actual = tested.getTrips().fold({ null }, { it })
            //then
            assertNotNull(actual)
            assertThat(actual!!.size, equalTo(2))
        }
    }

    @Test
    fun shouldRemoveTrip() {
        runBlocking {
            //given
            val tripOneId = tested.addOrUpdateTrip(tripOne).fold({ null }, { it })
            tested.addOrUpdateTrip(tripTwo)
            tested.addOrUpdateCategory(Trip(1), categoryOne)
            tested.addOrUpdateCategory(Trip(1), categoryTwo)
            tested.addOrUpdateItem(tripOne, Category(1), itemOne)
            tested.addOrUpdateItem(tripOne, Category(1), itemTwo)
            val toRemove =
                tested.getTrips().fold({ null }, { it })!!.find { it.id == tripOneId!!.toLong() }!!
            val categories = tested.getCategory(toRemove).fold({ null }, { it })
            //when
            tested.removeTrip(toRemove)
            val actual = tested.getTrips().fold({ null }, { it })
            //then
            val cleanedCategories = tested.getCategory(toRemove).fold({ null }, { it })
            val cleanedItems = ArrayList<Item>()
            for (category in categories!!) {
                cleanedItems.addAll(
                    tested.getItems(toRemove, category).fold({ ArrayList<Item>() }, { it })
                )
            }
            assertNotNull(actual)
            assertThat(actual!!.size, equalTo(1))
            assertThat(actual[0].title, equalTo(tripTwo.title))
            assertThat(cleanedCategories!!.size, equalTo(0))
            assertThat(cleanedItems.size, equalTo(0))
        }
    }

    @Test
    fun shouldInsertCategory() {
        runBlocking {
            //given
            tested.addOrUpdateTrip(tripOne)
            val trip = tested.getTrips().fold({ null }, { it[0] })
            //when
            tested.addOrUpdateCategory(trip!!, categoryOne)
            tested.addOrUpdateCategory(trip, categoryTwo)
            val actual = tested.getCategory(trip).fold({ null }, { it })
            //then
            assertNotNull(actual)
            assertThat(actual!!.size, equalTo(2))
            assertThat(actual[0].title, equalTo(categoryOne.title))
        }
    }

    @Test
    fun shouldRemoveCategory() {
        runBlocking {
            //given
            tested.addOrUpdateTrip(tripOne)
            val trip = tested.getTrips().fold({ null }, { it[0] })
            val categoryOneId =
                tested.addOrUpdateCategory(trip!!, categoryOne).fold({ "ERROR" }, { it })
            tested.addOrUpdateCategory(trip, categoryTwo)
            tested.addOrUpdateItem(tripOne, Category(1), itemOne)
            tested.addOrUpdateItem(tripOne, Category(1), itemTwo)
            val toRemove = tested.getCategory(trip).fold({ null }, { it })!!
                .find { it.id == categoryOneId.toLong() }
            //when
            tested.removeCategory(trip, toRemove!!)
            val actual = tested.getCategory(trip).fold({ null }, { it })
            //then
            val cleanedItems = tested.getItems(trip, toRemove).fold({ null }, { it })
            assertNotNull(actual)
            assertThat(actual!!.size, equalTo(1))
            assertThat(actual[0].title, equalTo(categoryTwo.title))
            assertThat(cleanedItems!!.size, equalTo(0))
        }
    }

    @Test
    fun shouldAddItem() {
        runBlocking {
            //given
            val tripId = tested.addOrUpdateTrip(tripOne).fold({ -1L }, { it.toLong() })
            val categoryIdOne =
                tested.addOrUpdateCategory(Trip(tripId), categoryOne).fold({ -1L }, { it.toLong() })
            val categoryIdTwo =
                tested.addOrUpdateCategory(Trip(tripId), categoryTwo).fold({ -1L }, { it.toLong() })
            //when
            tested.addOrUpdateItem(Trip(tripId), Category(categoryIdOne), itemOne)
            tested.addOrUpdateItem(Trip(tripId), Category(categoryIdOne), itemTwo)
            tested.addOrUpdateItem(Trip(tripId), Category(categoryIdTwo), itemTwo)
            val actual =
                tested.getItems(Trip(tripId), Category(categoryIdOne)).fold({ null }, { it })
            //then
            assertNotNull(actual)
            assertThat(actual!!.size, equalTo(2))
            assertThat(actual[0].name, equalTo(itemOne.name))
            assertThat(actual[0].isDone, equalTo(false))
        }
    }

    @Test
    fun shouldUpdateItem() {
        runBlocking {
            //given
            tested.addOrUpdateTrip(tripOne)
            tested.addOrUpdateCategory(Trip(1), categoryOne)
            tested.addOrUpdateCategory(Trip(1), categoryTwo)
            //when
            val itemOneId =
                tested.addOrUpdateItem(Trip(1), Category(1), itemOne).fold({ -1L }, { it.toLong() })
            tested.addOrUpdateItem(Trip(1), Category(1), itemTwo)
            tested.addOrUpdateItem(Trip(1), Category(2), itemTwo)
            tested.addOrUpdateItem(
                tripOne,
                Category(1),
                Item(itemOneId.toLong(), itemOne.name, true)
            )
            val actual = tested.getItems(Trip(1), Category(1)).fold({ ArrayList<Item>() }, { it })
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
            tested.addOrUpdateTrip(tripOne)
            tested.addOrUpdateCategory(Trip(1), categoryOne)
            val itemOneId =
                tested.addOrUpdateItem(Trip(1), Category(1), itemOne).fold({ -1L }, { it.toLong() })
            tested.addOrUpdateItem(Trip(1), Category(1), itemTwo)
            val toRemove =
                tested.getItems(Trip(1), Category(1)).fold({ null }, { it })!!
                    .find { it.id == itemOneId.toLong() }
            //when
            tested.removeItem(Trip(1), Category(1), toRemove!!)
            val actual = tested.getItems(Trip(1), Category(1)).fold({ ArrayList<Item>() }, { it })
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
            tested.addOrUpdateTrip(tripOne)
            tested.addOrUpdateCategory(Trip(1), categoryOne)
            tested.addOrUpdateCategory(Trip(1), categoryTwo)
            tested.addOrUpdateItem(tripOne, Category(1), itemOne)
            tested.addOrUpdateItem(tripOne, Category(1), itemTwo)
            //when
            val actual = tested.getTripDetail(1.toString()).fold({ null }, { it })
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

