package com.yusmle.restaurants.di

import com.yusmle.restaurants.features.restaurantslist.business.RestaurantRepository
import com.yusmle.restaurants.features.restaurantslist.business.RestaurantsListUseCase
import com.yusmle.restaurants.features.restaurantslist.data.LocalDataSource
import com.yusmle.restaurants.features.restaurantslist.data.RemoteDataSource
import com.yusmle.restaurants.features.restaurantslist.data.RestaurantDatabaseDataMapper
import com.yusmle.restaurants.features.restaurantslist.data.RestaurantNetworkDataMapper
import com.yusmle.restaurants.features.restaurantslist.data.RestaurantRepositoryImpl
import org.koin.dsl.module

/**
 * Provides application-wide dependencies
 */
val appModule = module {

    factory {
        RestaurantsListUseCase(get())
    }

    factory<RestaurantRepository> {
        RestaurantRepositoryImpl(get(), get(), get(), get())
    }

    factory {
        LocalDataSource(get())
    }

    factory {
        RemoteDataSource(get())
    }

    factory {
        RemoteDataSource(get())
    }

    factory {
        RestaurantDatabaseDataMapper()
    }

    factory {
        RestaurantNetworkDataMapper()
    }
}
