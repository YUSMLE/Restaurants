package com.yusmle.network.connectivity.internal

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import com.yusmle.network.connectivity.NetworkState

/**
 * Implementation of [ConnectivityManager.NetworkCallback]
 *
 * It stores every change of connectivity into [NetworkState].
 *
 * @see NetworkState
 */
internal class NetworkCallbackImp(
    private val stateHolder: NetworkStateImp
) : ConnectivityManager.NetworkCallback() {

    private fun updateConnectivity(isAvailable: Boolean, network: Network) {
        stateHolder.network = network
        stateHolder.isAvailable = isAvailable
    }

    /**
     * In case of a new network (wifi enabled), this is called first.
     */
    override fun onAvailable(network: Network) {
        // DEBUG
        Log.v(TAG, "New Network: [$network]")

        updateConnectivity(true, network)
    }

    /**
     * This is called several times in a row, as capabilities are added step by step.
     */
    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        // DEBUG
        Log.v(TAG, "Network Capability Changed: [$network] - $networkCapabilities")

        stateHolder.networkCapabilities = networkCapabilities
    }

    /**
     * This is called after.
     */
    override fun onLost(network: Network) {
        // DEBUG
        Log.v(TAG, "Network Lost: [$network]")

        updateConnectivity(false, network)
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        // DEBUG
        Log.v(TAG, "Network Link Properties Changed: [$network] - ${linkProperties.interfaceName}")

        stateHolder.linkProperties = linkProperties
    }

    override fun onUnavailable() {
        // DEBUG
        Log.v(TAG, "Network Unavailable")
    }

    override fun onLosing(network: Network, maxMsToLive: Int) {
        // DEBUG
        Log.v(TAG, "Losing With Network: [$network] - $maxMsToLive")
    }

    override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
        // DEBUG
        Log.v(TAG, "Network Blocked Status Changed: [$network] - $blocked")
    }

    companion object {
        private const val TAG = "NetworkCallbackImp"
    }
}
