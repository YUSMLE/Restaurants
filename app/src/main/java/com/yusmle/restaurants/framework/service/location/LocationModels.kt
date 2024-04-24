package com.yusmle.restaurants.framework.service.location

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    val latitude: Double,
    val longitude: Double
)
