package com.yusmle.restaurants.framework.service.location

import kotlinx.coroutines.flow.Flow

interface LocationDataStore {

    fun setCurrentLocation(location: Location)

    fun getCurrentLocation(): Location?

    fun getLocationStream(): Flow<Location?>

    fun clearLocation()
}
