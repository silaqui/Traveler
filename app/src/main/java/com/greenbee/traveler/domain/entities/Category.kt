package com.greenbee.traveler.domain.entities

data class Category(
    val id: String = "-1",
    val title: String = "",
    val items: ArrayList<Item> = ArrayList<Item>()
)