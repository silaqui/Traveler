package com.greenbee.traveler.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class TripDaoTest {
    private lateinit var tested: TripDao
    private lateinit var db: TravelerRoomDataBase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, TravelerRoomDataBase::class.java
        ).build()
        tested = db.tripDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun shouldGetEmptyList() {
        runBlocking {
            //given
            //when
            val actual = tested.getAll()
            //then
            assertNotNull(actual)
            assertEquals(actual.size, 0)
        }
    }

    @Test
    fun shouldInsert() {
        runBlocking {
            //given
            val tripEntity =
                TripEntity(
                    id = 1,
                    title = "title",
                    note = "note",
                    backgroundUrl = "url",
                    date = 0
                )
            //when
            tested.addOrUpdate(tripEntity)
            val actual = tested.getAll()
            //then
            assertNotNull(actual)
            assertEquals(actual.size, 1)
            assertThat(actual[0], equalTo(tripEntity))
        }
    }

    @Test
    fun shouldGetWithCategories() {
        runBlocking {
            //given
            val tripEntity =
                TripEntity(1, "Trip", "Trip note", "url", 0)
            db.tripDao().addOrUpdate(tripEntity)

            val categoryOne = CategoryEntity(1, 1, "Cat1")
            val categoryTwo = CategoryEntity(2, 1, "Cat2")
            db.categoryDao().addOrUpdate(categoryOne)
            db.categoryDao().addOrUpdate(categoryTwo)

            val item1 = ItemEntity(1,1,"Item 1 Cat 1",false)
            val item2 = ItemEntity(2,1,"Item 2 Cat 1",false)
            val item3 = ItemEntity(3,2,"Item 1 Cat 2",false)
            val item4 = ItemEntity(4,2,"Item 2 Cat 2",false)
            db.itemDao().addOrUpdate(item1)
            db.itemDao().addOrUpdate(item2)
            db.itemDao().addOrUpdate(item3)
            db.itemDao().addOrUpdate(item4)
            //when
            val actual = tested.getTripWithIdDetails(1)
            //then
            assertNotNull(actual)
            assertThat(actual!!.trip, equalTo(tripEntity))
            assertThat(actual.categories.size, equalTo(2))
            assertThat(actual.categories[0].items.size, equalTo(2))
        }
    }

    @Test
    fun shouldGetTrip() {
        runBlocking {
            //given
            val tripEntity =
                TripEntity(1, "Trip", "Trip note", "url", 0)
            db.tripDao().addOrUpdate(tripEntity)

            val categoryOne = CategoryEntity(1, 1, "Cat1")
            val categoryTwo = CategoryEntity(2, 1, "Cat2")
            db.categoryDao().addOrUpdate(categoryOne)
            db.categoryDao().addOrUpdate(categoryTwo)

            val item1 = ItemEntity(1,1,"Item 1 Cat 1",false)
            val item2 = ItemEntity(2,1,"Item 2 Cat 1",false)
            val item3 = ItemEntity(3,2,"Item 1 Cat 2",false)
            val item4 = ItemEntity(4,2,"Item 2 Cat 2",false)
            db.itemDao().addOrUpdate(item1)
            db.itemDao().addOrUpdate(item2)
            db.itemDao().addOrUpdate(item3)
            db.itemDao().addOrUpdate(item4)
            //when
            val actual = tested.getTripWithId(1)
            //then
            assertNotNull(actual)
            assertThat(actual, equalTo(tripEntity))
        }
    }

}