package com.yusmle.restaurants.framework.memory

import com.yusmle.restaurants.framework.service.location.Location
import com.yusmle.restaurants.framework.service.location.LocationDataStore
import kotlinx.coroutines.flow.MutableStateFlow

class InMemoryLocationDataStore : LocationDataStore {

    private val locationFlow = MutableStateFlow<Location?>(null)

    override fun setCurrentLocation(location: Location) {
        locationFlow.value = location
    }

    override fun getCurrentLocation(): Location? = locationFlow.value

    override fun getLocationStream() = locationFlow

    override fun clearLocation() {
        locationFlow.value = null
    }
}
