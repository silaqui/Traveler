package com.greenbee.traveler.domain.entities

data class Item(
    val id: Long = -1,
    val name: String = "",
    val isDone: Boolean = false
)