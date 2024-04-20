package com.yusmle.restaurants.features.restaurantslist.data

import com.yusmle.restaurants.common.DataSource

class RemoteDataSource(private val restaurantApiService: RestaurantApiService) : DataSource {

    suspend fun fetchRemoteRestaurantsList(
        pagingMetaData: String?,
        longitude: Float,
        latitude: Float
    ): RestaurantsSearchBundleResult {
        val locationString = longitude.toString().plus(",").plus(latitude.toString())

        // TODO("I know you are tired, but please avoid hard-coding! :)")
        return restaurantApiService.searchNearbyRestaurants(
            slug = "restaurant",
            location = locationString,
            camera = locationString,
            zoom = 18,
            pagingMetaData = pagingMetaData
        )
    }
}
