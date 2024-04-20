package com.yusmle.restaurants.features.restaurantslist.view

import com.yusmle.restaurants.features.restaurantslist.business.RestaurantsListUseCase
import com.yusmle.restaurants.common.foundation.StatefulIntentViewModel

class RestaurantsListViewModel(
    private val restaurantsListUseCase: RestaurantsListUseCase
) : StatefulIntentViewModel<RestaurantsListUserIntention, RestaurantsListViewState>(
    RestaurantsListViewState.Init
) {

    override fun onInitialize() {
        super.onInitialize()

        // Start getting restaurants list on view model initialization
        sendIntent(RestaurantsListUserIntention.GetRestaurantsList)
    }

    override suspend fun handleIntent(intent: RestaurantsListUserIntention) {
        when (intent) {
            is RestaurantsListUserIntention.GetRestaurantsList,
            is RestaurantsListUserIntention.GetMoreRestaurantsList,
            is RestaurantsListUserIntention.RetryGettingRestaurantsList,
            is RestaurantsListUserIntention.RetryGettingMoreRestaurantsList,
            is RestaurantsListUserIntention.RefreshRestaurantsList -> getRestaurantsList()
        }
    }

    private fun getRestaurantsList() {
        if (currentState is RestaurantsListViewState.Loading) return

        launch {
            setState {
                RestaurantsListViewState.Loading(hasNextPage, pagingMetaData, currentRestaurants)
            }

            runCatching {
                restaurantsListUseCase.execute(
                    RestaurantsListUseCase.Input(currentState.pagingMetaData)
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
