package com.yusmle.restaurants.common.helper.util

import com.yusmle.restaurants.framework.service.location.Location

/**
 * Calculates distance between to location in meters
 */
fun calculateRoundDistance(pointA: Location, pointB: Location): Long {
    val locationA = pointA.toAndroidLocation()
    val locationB = pointB.toAndroidLocation()
    return locationA.distanceTo(locationB).toLong()
}

fun Location.toAndroidLocation(): android.location.Location =
    android.location.Location(emptyString()).also {
        it.latitude = latitude
        it.longitude = longitude
    }
