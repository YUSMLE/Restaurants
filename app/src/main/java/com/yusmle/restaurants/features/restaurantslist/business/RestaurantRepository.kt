package com.yusmle.restaurants.features.restaurantslist.business

interface RestaurantRepository {

    suspend fun getPersistedRestaurantsList(): List<Restaurant>

    suspend fun fetchRemoteRestaurantsList(
        pagingMetaData: String?,
        longitude: Float,
        latitude: Float
    ): RestaurantsSearchBundle

    suspend fun deletePersistedRestaurantsList()

    suspend fun persistRemoteRestaurantsList(restaurants: List<Restaurant>)

    suspend fun getPersistedRestaurantDetails(name: String): Restaurant?
}
