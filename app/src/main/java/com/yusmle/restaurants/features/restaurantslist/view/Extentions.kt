package com.yusmle.restaurants.features.restaurantslist.view

import com.yusmle.restaurants.features.restaurantslist.business.Restaurant

val RestaurantsListViewState.currentRestaurants: List<Restaurant>
    get() = when (this) {
        RestaurantsListViewState.Init -> emptyList()
        is RestaurantsListViewState.Loading -> restaurants
        is RestaurantsListViewState.Loaded -> restaurants
        is RestaurantsListViewState.Failed -> restaurants
    }

fun List<Restaurant>.toListItem(): List<RestaurantListItem> {
    return this.map {
        RestaurantListItem.RestaurantItem(name = it.name)
    }
}
