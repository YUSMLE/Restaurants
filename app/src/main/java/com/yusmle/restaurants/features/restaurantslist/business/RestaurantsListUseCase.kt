package com.yusmle.restaurants.features.restaurantslist.business

import com.yusmle.restaurants.foundation.BaseUseCase

class RestaurantsListUseCase(
    private val restaurantRepository: RestaurantRepository
) : BaseUseCase<Unit, List<Restaurant>> {

    override suspend fun execute(input: Unit) = restaurantRepository.getPersistedRestaurantsList()
}
