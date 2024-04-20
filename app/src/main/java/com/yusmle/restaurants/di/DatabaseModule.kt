package com.yusmle.restaurants.di

import com.yusmle.restaurants.framework.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Provides database-related dependencies
 */
val databaseModule = module {

    single {
        AppDatabase.invoke(androidContext())
    }

    single {
        get<AppDatabase>().restaurantDao()
    }
}
