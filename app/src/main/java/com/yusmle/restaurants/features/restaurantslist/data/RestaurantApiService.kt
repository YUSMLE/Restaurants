package com.yusmle.restaurants.features.restaurantslist.data

import retrofit2.http.GET
import retrofit2.http.Query

interface RestaurantApiService {

    @GET("/bundle-search")
    suspend fun searchNearbyRestaurants(
        @Query("slug") slug: String,
        @Query("location") location: String,
        @Query("camera") camera: String,
        @Query("zoom") zoom: Int,
        @Query("paging_meta_data") pagingMetaData: String?,
    ): RestaurantsSearchBundleResult
}
