package com.yusmle.restaurants.features.restaurantslist.view

import android.annotation.SuppressLint
import android.util.Log
import com.yusmle.restaurants.common.foundation.StatefulIntentViewModel
import com.yusmle.restaurants.features.restaurantslist.business.LocationProvider
import com.yusmle.restaurants.features.restaurantslist.business.Restaurant
import com.yusmle.restaurants.features.restaurantslist.business.RestaurantsListUseCase
import com.yusmle.restaurants.framework.service.location.Location
import okhttp3.internal.toImmutableList

class RestaurantsListViewModel(
    private val restaurantsListUseCase: RestaurantsListUseCase,
    private val locationProvider: LocationProvider
) : StatefulIntentViewModel<RestaurantsListUserIntention, RestaurantsListViewState>(
    RestaurantsListViewState.Init
) {

    override fun onInitialize() {
        super.onInitialize()

        // Do some stuffs on view model initialization here...
        Log.d("RestaurantsListViewModel", "RestaurantsListViewState.Init")
    }

    @SuppressLint("MissingPermission")
    override suspend fun handleIntent(intent: RestaurantsListUserIntention) {
        when (intent) {
            is RestaurantsListUserIntention.LocationServiceRequirementsGranted,
            is RestaurantsListUserIntention.GetRestaurantsList,
            is RestaurantsListUserIntention.GetMoreRestaurantsList,
            is RestaurantsListUserIntention.RetryGettingRestaurantsList,
            is RestaurantsListUserIntention.RetryGettingMoreRestaurantsList,
            is RestaurantsListUserIntention.RefreshRestaurantsList ->
                locationProvider.getLastLocation {
                    getRestaurantsList(it, currentState == RestaurantsListViewState.Init)
                }
        }
    }

    private fun getRestaurantsList(location: Location?, offlineAllowed: Boolean) {
        if (currentState is RestaurantsListViewState.Loading ||
            currentState.hasNextPage.not()
        ) return

        launch {
            setState {
                RestaurantsListViewState.Loading(hasNextPage, pagingMetaData, currentRestaurants)
            }

            runCatching {
                restaurantsListUseCase.execute(
                    RestaurantsListUseCase.Input(
                        firstPage = currentState.currentRestaurants.isEmpty(),
                        offlineAllowed = offlineAllowed,
                        pagingMetaData = currentState.pagingMetaData,
                        longitude = location?.longitude ?: Double.MIN_VALUE,
                        latitude = location?.latitude ?: Double.MAX_VALUE
                    )
                )
            }.fold({
                setState {
                    RestaurantsListViewState.Loaded(
                        it.restaurantsSearchBundle.hasNextPage,
                        it.restaurantsSearchBundle.pagingMetaData,
                        mutableListOf<Restaurant>().apply {
                            addAll(currentRestaurants)
                            addAll(it.restaurantsSearchBundle.restaurants)
                        }.toImmutableList()
                    )
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
