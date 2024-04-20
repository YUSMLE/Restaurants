package com.yusmle.restaurants.di

import com.yusmle.restaurants.features.restaurantslist.view.RestaurantsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Provides view model instances
 */
val viewModelsModule = module {

    viewModel {
        RestaurantsListViewModel(get())
    }
}
