package com.yusmle.restaurants

import android.app.Application
import com.yusmle.restaurants.di.appModule
import com.yusmle.restaurants.di.databaseModule
import com.yusmle.restaurants.di.networkModules
import com.yusmle.restaurants.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        setupKoin()
    }

    private fun setupKoin() {
        val koinApp = startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(listOf(
                appModule,
                databaseModule,
                networkModules,
                viewModelsModule
            ))
        }
    }
}
