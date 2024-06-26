package com.yusmle.restaurants.common.helper.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.core.content.ContextCompat

@SuppressLint("ObsoleteSdkInt")
fun Context.isFineLocationPermissionGranted(): Boolean =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

fun Context.isAnyLocationProviderEnabled(): Boolean =
    (getSystemService(Context.LOCATION_SERVICE) as? LocationManager)
        ?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false
