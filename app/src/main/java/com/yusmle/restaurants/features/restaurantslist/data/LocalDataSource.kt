package com.yusmle.restaurants.features.restaurantslist.data

import com.yusmle.restaurants.common.DataSource

class LocalDataSource(private val restaurantDao: RestaurantDao) : DataSource {

    suspend fun persistRestaurantsList(restaurants: List<RestaurantEntity>) {
        restaurantDao.insertAll(restaurants)
    }

    suspend fun getRestaurantsList(): List<RestaurantEntity> {
        return restaurantDao.getAll()
    }

    suspend fun getRestaurant(name: String): RestaurantEntity? {
        return restaurantDao.getRestaurantByName(name)
    }

    suspend fun deleteRestaurantsList() {
        restaurantDao.deleteAll()
    }
}
