package com.yusmle.restaurants.features.restaurantslist.data

import com.yusmle.restaurants.common.DataMapper
import com.yusmle.restaurants.features.restaurantslist.business.Restaurant

class RestaurantDatabaseDataMapper : DataMapper<Restaurant, RestaurantEntity>() {

    override fun transformToEntity(model: Restaurant) = with(model) {
        RestaurantEntity(
            name = name,
            phoneNumber = phoneNumber,
            reviewRate = reviewRate,
            address = address,
            distance = distance
        )
    }

    override fun transformToModel(entity: RestaurantEntity) = with(entity) {
        Restaurant(
            name = name,
            phoneNumber = phoneNumber,
            reviewRate = reviewRate,
            address = address,
            distance = distance
        )
    }
}
