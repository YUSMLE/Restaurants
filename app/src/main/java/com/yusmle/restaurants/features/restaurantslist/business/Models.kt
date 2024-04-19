package com.yusmle.restaurants.features.restaurantslist.business

data class Restaurant(
    val name: String,
    val phoneNumber: String,
    val reviewRate: Int,
    val address: String,
    val distance: String
)
