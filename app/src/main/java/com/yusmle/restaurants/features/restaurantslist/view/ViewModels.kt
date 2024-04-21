package com.yusmle.restaurants.features.restaurantslist.view

import com.yusmle.restaurants.features.restaurantslist.business.Restaurant

sealed class RestaurantsListUserIntention {

    object GetRestaurantsList : RestaurantsListUserIntention()

    object GetMoreRestaurantsList : RestaurantsListUserIntention()

    object RetryGettingRestaurantsList : RestaurantsListUserIntention()

    object RetryGettingMoreRestaurantsList : RestaurantsListUserIntention()

    object RefreshRestaurantsList : RestaurantsListUserIntention()
}

sealed class RestaurantsListViewState(
    open val hasNextPage: Boolean,
    open val pagingMetaData: String? = null
) {

    object Init : RestaurantsListViewState(true)

    data class Loading(
        override val hasNextPage: Boolean,
        override val pagingMetaData: String?,
        val restaurants: List<Restaurant>
    ) : RestaurantsListViewState(hasNextPage, pagingMetaData)

    data class Loaded(
        override val hasNextPage: Boolean,
        override val pagingMetaData: String?,
        val restaurants: List<Restaurant>
    ) : RestaurantsListViewState(hasNextPage, pagingMetaData)

    data class Failed(
        override val hasNextPage: Boolean,
        override val pagingMetaData: String?,
        val restaurants: List<Restaurant>,
        val message: String?,
        val throwable: Throwable?
    ) : RestaurantsListViewState(hasNextPage, pagingMetaData)
}
