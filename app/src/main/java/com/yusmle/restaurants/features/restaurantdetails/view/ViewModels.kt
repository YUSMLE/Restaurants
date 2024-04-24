package com.yusmle.restaurants.features.restaurantdetails.view

import com.yusmle.restaurants.features.restaurantslist.business.Restaurant

sealed class RestaurantDetailsUserIntention {

    object GetRestaurantDetails : RestaurantDetailsUserIntention()
}

sealed class RestaurantDetailsViewState(
    open val restaurant: Restaurant?
) {

    object Init : RestaurantDetailsViewState(null)

    object Loading : RestaurantDetailsViewState(null)

    data class Loaded(
        override val restaurant: Restaurant?
    ) : RestaurantDetailsViewState(restaurant)

    data class Failed(
        val message: String?,
        val throwable: Throwable?
    ) : RestaurantDetailsViewState(null)
}
