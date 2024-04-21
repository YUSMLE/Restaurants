package com.yusmle.restaurants.features.restaurantslist.view

import android.annotation.SuppressLint
import com.yusmle.restaurants.common.foundation.StatefulIntentViewModel
import com.yusmle.restaurants.features.restaurantslist.business.RestaurantsListUseCase
import com.yusmle.restaurants.framework.service.location.Location
import com.yusmle.restaurants.framework.service.location.LocationTrackerService

class RestaurantsListViewModel(
    private val restaurantsListUseCase: RestaurantsListUseCase,
    private val locationTrackerService: LocationTrackerService
) : StatefulIntentViewModel<RestaurantsListUserIntention, RestaurantsListViewState>(
    RestaurantsListViewState.Init
) {

    override fun onInitialize() {
        super.onInitialize()

        // Start getting restaurants list on view model initialization
        sendIntent(RestaurantsListUserIntention.GetRestaurantsList)
    }

    @SuppressLint("MissingPermission")
    override suspend fun handleIntent(intent: RestaurantsListUserIntention) {
        when (intent) {
            is RestaurantsListUserIntention.GetRestaurantsList,
            is RestaurantsListUserIntention.GetMoreRestaurantsList,
            is RestaurantsListUserIntention.RetryGettingRestaurantsList,
            is RestaurantsListUserIntention.RetryGettingMoreRestaurantsList,
            is RestaurantsListUserIntention.RefreshRestaurantsList ->
                locationTrackerService.getLastLocation {
                    getRestaurantsList(it)
                }
        }
    }

    private fun getRestaurantsList(location: Location?) {
        if (currentState is RestaurantsListViewState.Loading) return

        launch {
            setState {
                RestaurantsListViewState.Loading(hasNextPage, pagingMetaData, currentRestaurants)
            }

            runCatching {
                restaurantsListUseCase.execute(
                    RestaurantsListUseCase.Input(
                        currentState.pagingMetaData,
                        location?.longitude ?: Double.MIN_VALUE,
                        location?.latitude ?: Double.MAX_VALUE
                    )
                )
            }.fold({
                setState {
                    RestaurantsListViewState.Loaded(hasNextPage,
                        pagingMetaData,
                        currentRestaurants)
                }
            }, {
                // DEBUG
                it.printStackTrace()

                setState {
                    RestaurantsListViewState.Failed(
                        hasNextPage,
                        pagingMetaData,
                        currentRestaurants,
                        it.message,
                        it
                    )
                }
            })
        }
    }
}
