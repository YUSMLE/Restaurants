package com.yusmle.restaurants.features.restaurantdetails.business

import com.yusmle.restaurants.common.foundation.BaseUseCase
import com.yusmle.restaurants.features.restaurantslist.business.Restaurant
import com.yusmle.restaurants.features.restaurantslist.business.RestaurantRepository

class GetRestaurantDetailsUseCase(
    private val restaurantRepository: RestaurantRepository
) : BaseUseCase<GetRestaurantDetailsUseCase.Input, GetRestaurantDetailsUseCase.Output> {

    override suspend fun execute(input: Input): Output =
        Output(restaurantRepository.getPersistedRestaurantDetails(input.name))

    data class Input(
        val name: String
    )

    data class Output(
        val restaurant: Restaurant?
    )
}
