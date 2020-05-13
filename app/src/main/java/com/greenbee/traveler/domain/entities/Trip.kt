package com.greenbee.traveler.domain.entities

data class Trip(
    val id: Long = -1,
    val title: String = "",
    val note: String = "",
    val backgroundUrl: String = "",
    val date: Long = System.currentTimeMillis(),
    val categories: ArrayList<Category> = ArrayList<Category>()
) {
}
