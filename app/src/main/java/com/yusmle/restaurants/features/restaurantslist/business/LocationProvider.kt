package com.yusmle.restaurants.features.restaurantslist.business

import com.google.android.gms.tasks.CancellationToken
import com.yusmle.restaurants.framework.service.location.Location

interface LocationProvider {

    /**
     * Gets a location estimate more quickly and minimizes battery usage that can be attributed
     * to your app. However, the location information might be out of date, if no other clients
     * have actively used location recently.
     */
    fun getLastLocation(listener: (location: Location?) -> Unit)

    /**
     * Gets a fresher, more accurate location more consistently. However, this method can cause
     * active location computation to occur on the device.
     */
    fun getCurrentLocation(
        cancellationToken: CancellationToken,
        listener: (location: Location?) -> Unit
    )
}
