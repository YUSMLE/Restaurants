package com.yusmle.restaurants.features.restaurantdetails.view

import android.annotation.SuppressLint
import android.util.Log
import com.yusmle.restaurants.common.foundation.StatefulIntentViewModel
import com.yusmle.restaurants.features.restaurantdetails.business.GetRestaurantDetailsUseCase

class RestaurantDetailsViewModel(
    private val restaurantName: String,
    private val restaurantDetailsUseCase: GetRestaurantDetailsUseCase
) : StatefulIntentViewModel<RestaurantDetailsUserIntention, RestaurantDetailsViewState>(
    RestaurantDetailsViewState.Init
) {

    override fun onInitialize() {
        super.onInitialize()

        // Do some stuffs on view model initialization here...
        Log.d("RestaurantDetailsViewModel", "RestaurantDetailsViewState.Init")
    }

    @SuppressLint("MissingPermission")
    override suspend fun handleIntent(intent: RestaurantDetailsUserIntention) {
        if (intent == RestaurantDetailsUserIntention.GetRestaurantDetails) {
            getRestaurantDetails(restaurantName)
        }
        else throw IllegalArgumentException()
    }

    private fun getRestaurantDetails(name: String) {
        if (currentState == RestaurantDetailsViewState.Loading) return

        launch {
            setState {
                RestaurantDetailsViewState.Loading
            }

            runCatching {
                restaurantDetailsUseCase.execute(
                    GetRestaurantDetailsUseCase.Input(name)
                )
            }.fold({
                setState {
                    RestaurantDetailsViewState.Loaded(it.restaurant)
                }
            }, {
                // DEBUG
                it.printStackTrace()

                setState {
                    RestaurantDetailsViewState.Failed(it.message, it)
                }
            })
        }
    }
}
