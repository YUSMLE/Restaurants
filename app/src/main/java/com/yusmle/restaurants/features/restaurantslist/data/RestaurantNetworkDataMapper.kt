package com.yusmle.restaurants.features.restaurantslist.data

import com.yusmle.restaurants.common.DataMapper
import com.yusmle.restaurants.features.restaurantslist.business.Restaurant

class RestaurantNetworkDataMapper : DataMapper<Restaurant, RestaurantResult>() {

    override fun transformToEntity(model: Restaurant) = with(model) {
        RestaurantResult(
            name = name,
            phoneNumber = phoneNumber,
            reviewRate = reviewRate,
            address = address,
            distance = distance
        )
    }

    override fun transformToModel(entity: RestaurantResult) = with(entity) {
        Restaurant(
            name = name,
            phoneNumber = phoneNumber,
            reviewRate = reviewRate,
            address = address,
            distance = distance
        )
    }
}
