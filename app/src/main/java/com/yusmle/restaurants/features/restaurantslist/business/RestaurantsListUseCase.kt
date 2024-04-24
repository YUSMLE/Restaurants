package com.yusmle.restaurants.features.restaurantslist.business

import android.util.Log
import com.yusmle.network.connectivity.ConnectivityStateHolder
import com.yusmle.restaurants.common.error.NoConnectivityException
import com.yusmle.restaurants.common.foundation.BaseUseCase
import com.yusmle.restaurants.common.helper.util.calculateRoundDistance
import com.yusmle.restaurants.framework.preferences.SettingsManager
import com.yusmle.restaurants.framework.service.location.Location

class RestaurantsListUseCase(
    private val restaurantRepository: RestaurantRepository,
    private val settingsManager: SettingsManager
) : BaseUseCase<RestaurantsListUseCase.Input, RestaurantsListUseCase.Output> {

    override suspend fun execute(input: Input): Output {
        val lastSyncedLocation = settingsManager.syncedLocationPreferences.value
        val distance = lastSyncedLocation?.let {
            calculateRoundDistance(it, Location(input.latitude, input.longitude))
        } ?: Long.MAX_VALUE

        // DEBUG
        Log.d("RestaurantsListUseCase", "Distance from last synced location: ${distance}m")

        return if (ConnectivityStateHolder.isConnected || distance >= 10) {
            if (input.firstPage)
                restaurantRepository.deletePersistedRestaurantsList()

            if (ConnectivityStateHolder.isConnected.not())
                throw NoConnectivityException()

            val remoteResult = restaurantRepository.fetchRemoteRestaurantsList(
                input.pagingMetaData,
                input.longitude,
                input.latitude
            )

            restaurantRepository.persistRemoteRestaurantsList(remoteResult.restaurants)
            settingsManager.syncedLocationPreferences.value =
                Location(input.latitude, input.longitude)

            Output(remoteResult)
        }
        else if (input.offlineAllowed) {
            val localResult = restaurantRepository.getPersistedRestaurantsList()

            if (localResult.isNotEmpty())
                Output(RestaurantsSearchBundle(false, null, localResult))
            else
                // We should fetch remote restaurants list, but no connectivity!
                throw NoConnectivityException()
        }
        else {
            throw NoConnectivityException()
        }
    }

    data class Input(
        val firstPage: Boolean,
        val offlineAllowed: Boolean,
        val pagingMetaData: String?,
        val longitude: Double,
        val latitude: Double
    )

    data class Output(
        val restaurantsSearchBundle: RestaurantsSearchBundle
    )
}
