package com.yusmle.restaurants.di

import android.os.Build
import com.yusmle.restaurants.Constants
import com.yusmle.restaurants.features.restaurantslist.data.RestaurantApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Provides network-related dependencies
 */
val networkModules = module {

    factory<Interceptor>(named<HttpLoggingInterceptor>()) {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    factory {
        OkHttpClient.Builder()
            .addInterceptor(get<Interceptor>(named<HttpLoggingInterceptor>()))
            .callTimeout(Constants.NETWORK_TIMEOUT_CONNECT, TimeUnit.SECONDS)
            .readTimeout(Constants.NETWORK_TIMEOUT_READ, TimeUnit.SECONDS)
            .writeTimeout(Constants.NETWORK_TIMEOUT_WRITE, TimeUnit.SECONDS)
            .apply {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) certificatePinner(get())
            }
            .build()
    }

    single {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get())
            .baseUrl(Constants.NETWORK_BASE_URL)
            .build()
    }

    single {
        get<Retrofit>().create(RestaurantApiService::class.java)
    }
}
