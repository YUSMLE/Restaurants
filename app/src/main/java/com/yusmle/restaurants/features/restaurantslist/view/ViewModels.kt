package com.yusmle.restaurants.features.restaurantslist.view

import com.yusmle.restaurants.features.restaurantslist.business.Restaurant

sealed class RestaurantsListUserIntention {

    object LocationServiceRequirementsGranted : RestaurantsListUserIntention()

    object GetRestaurantsList : RestaurantsListUserIntention()

    object GetMoreRestaurantsList : RestaurantsListUserIntention()

    object RetryGettingRestaurantsList : RestaurantsListUserIntention()

    object RetryGettingMoreRestaurantsList : RestaurantsListUserIntention()

    object RefreshRestaurantsList : RestaurantsListUserIntention()
}

sealed class RestaurantsListViewState(
    open val restaurants: List<Restaurant>,
    open val hasNextPage: Boolean,
    open val pagingMetaData: String? = null
) {

    object Init : RestaurantsListViewState(listOf(), true)

    data class Loading(
        override val restaurants: List<Restaurant>,
        override val hasNextPage: Boolean,
        override val pagingMetaData: String?,
        val refreshing: Boolean = false
    ) : RestaurantsListViewState(restaurants, hasNextPage, pagingMetaData)

    data class Loaded(
        override val restaurants: List<Restaurant>,
        override val hasNextPage: Boolean,
        override val pagingMetaData: String?
    ) : RestaurantsListViewState(restaurants, hasNextPage, pagingMetaData)

    data class Failed(
        override val restaurants: List<Restaurant>,
        override val hasNextPage: Boolean,
        override val pagingMetaData: String?,
        val message: String?,
        val throwable: Throwable?
    ) : RestaurantsListViewState(restaurants, hasNextPage, pagingMetaData)
}
