package com.yusmle.restaurants.common.helper.util

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

/**
 * We use this method, that can prevent IllegalException of Navigation Component.
 */
fun Fragment.navigateSafe(destination: NavDirections) = with(findNavController()) {
    currentDestination?.getAction(destination.actionId)?.let {
        navigate(destination)
    } ?: run {
        // DEBUG
        Log.w("Navigation", "Unable to navigate to ${destination.actionId}")
    }
}
