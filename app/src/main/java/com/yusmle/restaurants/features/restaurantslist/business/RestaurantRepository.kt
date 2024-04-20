package com.yusmle.restaurants.features.restaurantslist.business

interface RestaurantRepository {

    suspend fun getPersistedRestaurantsList(): List<Restaurant>

    suspend fun fetchRemoteRestaurantsList(pagingMetaData: String?): RestaurantsSearchResult

    suspend fun persistRemoteRestaurantsList(restaurants: List<Restaurant>)

    suspend fun getPersistedRestaurantDetails(name: String): Restaurant
}
