package com.yusmle.restaurants.features.restaurantslist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurant(entity: RestaurantEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurants(vararg entities: RestaurantEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<RestaurantEntity>)

    @Query("SELECT * FROM RestaurantEntity")
    suspend fun getAll(): List<RestaurantEntity>

    @Query("SELECT * FROM RestaurantEntity WHERE name LIKE :name LIMIT 1")
    suspend fun getRestaurantByName(name: String): RestaurantEntity?

    @Query("DELETE FROM RestaurantEntity")
    suspend fun deleteAll()
}
