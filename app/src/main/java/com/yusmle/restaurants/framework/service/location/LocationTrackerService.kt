package com.yusmle.restaurants.framework.service.location

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.yusmle.restaurants.common.helper.util.isFineLocationPermissionGranted

/**
 * Background service using FusedLocation API, get current location through specific interval.
 * Start based on some circumstances: GPS gets enabled, Connectivity gets enabled,
 * And also the required permission [ACCESS_FINE_LOCATION] is granted.
 *
 * @see [LocationTrackerServiceHandler] interactor.
 */

class LocationTrackerService {

    /**
     * Using the Google Play services location APIs, your app can request the last known location
     * of the user's device. In most cases, you are interested in the user's current location,
     * which is usually equivalent to the last known location of the device.
     *
     * The [FusedLocationProviderClient] provides several methods to retrieve device location
     * information. You can choose from one of the following, depending on your app's use case:
     *   • [getLastLocation()]
     *   • [getCurrentLocation()]
     *
     * This is the recommended way to get a fresh location, whenever possible, and is safer than
     * alternatives like starting and managing location updates yourself using
     * [requestLocationUpdates()]. If your app calls [requestLocationUpdates()], your app can
     * sometimes consume large amounts of power if location isn't available, or if the request isn't
     * stopped correctly after obtaining a fresh location.
     *
     * It's recommended to @see [https://developer.android.com/training/location/retrieve-current].
     */
    companion object {

        private const val TAG = "LocationTrackerService"

        /**
         * Gets a location estimate more quickly and minimizes battery usage that can be attributed
         * to your app. However, the location information might be out of date, if no other clients
         * have actively used location recently.
         */
        @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        fun getLastLocation(context: Context, listener: (location: Location?) -> Unit) {
            if (context.isFineLocationPermissionGranted()) {
                LocationServices.getFusedLocationProviderClient(context)
                    .lastLocation.addOnSuccessListener {
                        it?.let {
                            // DEBUG
                            Log.d(TAG,
                                "The last known location is: ${it.latitude}, ${it.longitude}")

                            listener(Location(it.latitude, it.longitude))
                        } ?: run {
                            // DEBUG
                            Log.d(TAG, "The last known location is: NULL")

                            listener(null)
                        }
                    }
            }
            else listener(null)
        }

        /**
         * Gets a fresher, more accurate location more consistently. However, this method can cause
         * active location computation to occur on the device
         */
        @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        fun getCurrentLocation(
            context: Context,
            cancellationToken: CancellationToken,
            listener: (location: Location?) -> Unit
        ) {
            if (context.isFineLocationPermissionGranted()) {
                LocationServices.getFusedLocationProviderClient(context)
                    .getCurrentLocation(
                        LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
                        cancellationToken
                    ).addOnSuccessListener {
                        it?.let {
                            // DEBUG
                            Log.d(TAG, "The current location is: ${it.latitude}, ${it.longitude}")

                            listener(Location(it.latitude, it.longitude))
                        } ?: run {
                            // DEBUG
                            Log.d(TAG, "The current location is: NULL")

                            listener(null)
                        }
                    }
            }
            else listener(null)
        }
    }
}
