package com.yusmle.restaurants.di

import com.yusmle.restaurants.common.DispatcherProvider
import com.yusmle.restaurants.common.DispatcherProviderImpl
import com.yusmle.restaurants.features.restaurantdetails.business.GetRestaurantDetailsUseCase
import com.yusmle.restaurants.features.restaurantslist.business.ClearRestaurantsListUseCase
import com.yusmle.restaurants.features.restaurantslist.business.LocationProvider
import com.yusmle.restaurants.features.restaurantslist.business.RestaurantRepository
import com.yusmle.restaurants.features.restaurantslist.business.RestaurantsListUseCase
import com.yusmle.restaurants.features.restaurantslist.data.LocalDataSource
import com.yusmle.restaurants.features.restaurantslist.data.LocationProviderImpl
import com.yusmle.restaurants.features.restaurantslist.data.RemoteDataSource
import com.yusmle.restaurants.features.restaurantslist.data.RestaurantDatabaseDataMapper
import com.yusmle.restaurants.features.restaurantslist.data.RestaurantNetworkDataMapper
import com.yusmle.restaurants.features.restaurantslist.data.RestaurantRepositoryImpl
import com.yusmle.restaurants.framework.memory.InMemoryLocationDataStore
import com.yusmle.restaurants.framework.preferences.SettingsManager
import com.yusmle.restaurants.framework.service.location.LocationDataStore
import com.yusmle.restaurants.framework.service.location.LocationTrackerService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Provides application-wide dependencies
 */
val appModule = module {

    single<DispatcherProvider> {
        DispatcherProviderImpl()
    }

    factory {
        RestaurantsListUseCase(get(), get())
    }

    factory {
        ClearRestaurantsListUseCase(get())
    }

    factory {
        GetRestaurantDetailsUseCase(get())
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
        RestaurantDatabaseDataMapper()
    }

    factory {
        RestaurantNetworkDataMapper()
    }

    single {
        LocationTrackerService(androidContext())
    }

    single<LocationDataStore> {
        InMemoryLocationDataStore()
    }

    factory<LocationProvider> {
        LocationProviderImpl(get())
    }

    single {
        SettingsManager(androidContext(), get())
    }
}
