package com.greenbee.traveler.domain.entities

data class Trip(
    val id: String = "-1",
    val title: String = "",
    val note: String = "",
    val backgroundUrl: String = "cabin",
    val date: Long = System.currentTimeMillis(),
    val categories: ArrayList<Category> = ArrayList<Category>()
)
