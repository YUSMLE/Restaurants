package com.yusmle.restaurants.features.restaurantslist.business

data class RestaurantsSearchBundle(
    val hasNextPage: Boolean,
    val pagingMetaData: String?,
    val restaurants: List<Restaurant>
)

data class Restaurant(
    val name: String,
    val phoneNumber: String?,
    val reviewRate: Float,
    val address: String,
    val distance: String
)
