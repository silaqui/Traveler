package com.greenbee.traveler.domain.entities

class Category (
    val id: Long = -1,
    val title: String = "",
    val items: ArrayList<Item> = ArrayList<Item>()
)