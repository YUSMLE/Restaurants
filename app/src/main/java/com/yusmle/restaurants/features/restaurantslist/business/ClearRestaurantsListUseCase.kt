package com.yusmle.restaurants.features.restaurantslist.business

import com.yusmle.restaurants.common.foundation.BaseUseCase

class ClearRestaurantsListUseCase(
    private val restaurantRepository: RestaurantRepository
) : BaseUseCase<Unit, Unit> {

    override suspend fun execute(input: Unit): Unit {
        restaurantRepository.deletePersistedRestaurantsList()
    }
}
