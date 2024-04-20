package com.yusmle.restaurants.features.restaurantslist.business

import com.yusmle.restaurants.foundation.BaseUseCase

class RestaurantsListUseCase(
    private val restaurantRepository: RestaurantRepository
) : BaseUseCase<RestaurantsListUseCase.Input, RestaurantsListUseCase.Output> {

    override suspend fun execute(input: Input) =
        Output(restaurantRepository.fetchRemoteRestaurantsList(input.pagingMetaData))

    data class Input(
        val pagingMetaData: String?
    )

    data class Output(
        val restaurantsSearchResult: RestaurantsSearchResult
    )
}
