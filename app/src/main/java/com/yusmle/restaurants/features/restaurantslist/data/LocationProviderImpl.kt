package com.yusmle.restaurants.features.restaurantslist.data

import android.Manifest
import androidx.annotation.RequiresPermission
import com.google.android.gms.tasks.CancellationToken
import com.yusmle.restaurants.features.restaurantslist.business.LocationProvider
import com.yusmle.restaurants.framework.service.location.Location
import com.yusmle.restaurants.framework.service.location.LocationTrackerService

class LocationProviderImpl(
    private val locationTrackerService: LocationTrackerService
) : LocationProvider {

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    override fun getLastLocation(listener: (location: Location?) -> Unit) {
        locationTrackerService.getLastLocation(listener)
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    override fun getCurrentLocation(
        cancellationToken: CancellationToken,
        listener: (location: Location?) -> Unit
    ) {
        locationTrackerService.getCurrentLocation(cancellationToken, listener)
    }
}
