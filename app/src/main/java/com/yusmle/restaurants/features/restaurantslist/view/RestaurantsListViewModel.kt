package com.yusmle.restaurants.features.restaurantslist.view

import android.annotation.SuppressLint
import android.util.Log
import com.yusmle.restaurants.common.foundation.StatefulIntentViewModel
import com.yusmle.restaurants.features.restaurantslist.business.ClearRestaurantsListUseCase
import com.yusmle.restaurants.features.restaurantslist.business.LocationProvider
import com.yusmle.restaurants.features.restaurantslist.business.Restaurant
import com.yusmle.restaurants.features.restaurantslist.business.RestaurantsListUseCase
import com.yusmle.restaurants.framework.service.location.Location
import okhttp3.internal.toImmutableList

class RestaurantsListViewModel(
    private val restaurantsListUseCase: RestaurantsListUseCase,
    private val clearRestaurantsListUseCase: ClearRestaurantsListUseCase,
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
            is RestaurantsListUserIntention.RetryGettingMoreRestaurantsList ->
                locationProvider.getLastLocation {
                    getRestaurantsList(it, currentState == RestaurantsListViewState.Init)
                }
            is RestaurantsListUserIntention.RefreshRestaurantsList ->
                refreshRestaurantsList()
        }
    }

    private fun getRestaurantsList(
        location: Location?,
        offlineAllowed: Boolean,
        refreshing: Boolean = false
    ) {
        if (currentState is RestaurantsListViewState.Loading ||
            currentState.hasNextPage.not()
        ) return

        launch {
            setState {
                RestaurantsListViewState.Loading(
                    currentRestaurants,
                    hasNextPage,
                    pagingMetaData,
                    refreshing
                )
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
                        mutableListOf<Restaurant>().apply {
                            addAll(currentRestaurants)
                            addAll(it.restaurantsSearchBundle.restaurants)
                        }.toImmutableList(),
                        it.restaurantsSearchBundle.hasNextPage,
                        it.restaurantsSearchBundle.pagingMetaData
                    )
                }
            }, {
                // DEBUG
                it.printStackTrace()

                setState {
                    RestaurantsListViewState.Failed(
                        currentRestaurants,
                        hasNextPage,
                        pagingMetaData,
                        it.message,
                        it
                    )
                }
            })
        }
    }

    private fun refreshRestaurantsList() {
        if (currentState is RestaurantsListViewState.Loading) return

        launch {
            setState {
                RestaurantsListViewState.Init
            }

            clearRestaurantsListUseCase.execute(Unit)

            locationProvider.getLastLocation {
                getRestaurantsList(it, offlineAllowed = false, refreshing = true)
            }
        }
    }
}
