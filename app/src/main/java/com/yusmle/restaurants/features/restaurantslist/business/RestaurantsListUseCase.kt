package com.yusmle.restaurants.features.restaurantslist.business

import com.yusmle.restaurants.common.foundation.BaseUseCase

class RestaurantsListUseCase(
    private val restaurantRepository: RestaurantRepository
) : BaseUseCase<RestaurantsListUseCase.Input, RestaurantsListUseCase.Output> {

    override suspend fun execute(input: Input) =
        Output(restaurantRepository.fetchRemoteRestaurantsList(
            input.pagingMetaData,
            input.longitude,
            input.latitude
        ))

    data class Input(
        val pagingMetaData: String?,
        val longitude: Double,
        val latitude: Double
    )

    data class Output(
        val restaurantsSearchBundle: RestaurantsSearchBundle
    )
}
