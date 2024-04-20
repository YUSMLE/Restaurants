package com.yusmle.restaurants.features.restaurantslist.data

import com.yusmle.restaurants.features.restaurantslist.business.Restaurant
import com.yusmle.restaurants.features.restaurantslist.business.RestaurantRepository
import com.yusmle.restaurants.features.restaurantslist.business.RestaurantsSearchBundle

class RestaurantRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val restaurantDatabaseDataMapper: RestaurantDatabaseDataMapper,
    private val restaurantNetworkDataMapper: RestaurantNetworkDataMapper
) : RestaurantRepository {

    override suspend fun getPersistedRestaurantsList() =
        localDataSource.getRestaurantsList().let {
            restaurantDatabaseDataMapper.transformToModels(it)
        }

    override suspend fun fetchRemoteRestaurantsList(
        pagingMetaData: String?,
        longitude: Float,
        latitude: Float
    ) = remoteDataSource.fetchRemoteRestaurantsList(pagingMetaData, longitude, latitude).let {
        RestaurantsSearchBundle(
            hasNextPage = it.pagingMeta.hasNextPage,
            pagingMetaData = it.pagingMeta.pagingMetaData,
            restaurants = restaurantNetworkDataMapper.transformToModels(it.results)
        )
    }

    override suspend fun deletePersistedRestaurantsList() =
        localDataSource.deleteRestaurantsList()

    override suspend fun persistRemoteRestaurantsList(restaurants: List<Restaurant>) =
        localDataSource.persistRestaurantsList(
            restaurantDatabaseDataMapper.transformToEntities(restaurants)
        )

    override suspend fun getPersistedRestaurantDetails(name: String) =
        localDataSource.getRestaurant(name)?.let {
            restaurantDatabaseDataMapper.transformToModel(it)
        }
}
