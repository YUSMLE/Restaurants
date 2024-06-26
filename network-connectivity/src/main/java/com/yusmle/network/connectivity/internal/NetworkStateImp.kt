package com.yusmle.network.connectivity.internal

import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import com.yusmle.network.connectivity.ConnectivityStateHolder
import com.yusmle.network.connectivity.NetworkState

/**
 * This is a static implementation of [NetworkState].
 *
 * It holds the network states and is editable, but it's only usable from this file.
 */
internal class NetworkStateImp(callback: (NetworkState, NetworkEvent) -> Unit) : NetworkState {

    private var notify: (NetworkEvent) -> Unit = { e: NetworkEvent -> callback(this, e) }

    override var isAvailable: Boolean = false
        set(value) {
            val old = field
            val odlIConnected = ConnectivityStateHolder.isConnected
            field = value
            notify(NetworkEvent.AvailabilityEvent(this, old, odlIConnected))
        }

    override var network: Network? = null

    override var linkProperties: LinkProperties? = null
        set(value) {
            val old = field
            field = value
            notify(NetworkEvent.LinkPropertyChangeEvent(this, old))
        }

    override var networkCapabilities: NetworkCapabilities? = null
        set(value) {
            val old = field
            field = value
            notify(NetworkEvent.NetworkCapabilityEvent(this, old))
        }
}
