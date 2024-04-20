package com.yusmle.restaurants.features.restaurantslist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database DTOs / Models here
 */

@Entity
data class RestaurantEntity(
    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,

    @ColumnInfo(name = "review_rate")
    val reviewRate: Int,

    @ColumnInfo(name = "address")
    val address: String,

    @ColumnInfo(name = "distance")
    val distance: String
)
