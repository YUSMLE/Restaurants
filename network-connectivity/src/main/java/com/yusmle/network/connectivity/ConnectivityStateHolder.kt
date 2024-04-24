package com.yusmle.network.connectivity

import android.app.Application
import android.content.Context
import android.net.*
import com.yusmle.network.connectivity.internal.ActivityLifecycleCallbacksImp
import com.yusmle.network.connectivity.internal.NetworkCallbackImp
import com.yusmle.network.connectivity.internal.NetworkEvent
import com.yusmle.network.connectivity.internal.NetworkStateImp

object ConnectivityStateHolder : ConnectivityState {

    private val networkStatesSet: MutableSet<NetworkState> = mutableSetOf()

    override val networkStates: Iterable<NetworkState>
        get() = networkStatesSet

    private fun networkEventHandler(state: NetworkState, event: NetworkEvent) {
        when (event) {
            is NetworkEvent.AvailabilityEvent -> {
                if (isConnected != event.oldNetworkAvailability) {
                    NetworkEvents.notify(Event.ConnectivityEvent(state.isAvailable))
                }
            }
            else -> {
                /* Nothing!! */
            }
        }
    }

    /**
     * This starts the broadcast of network events to NetworkState and
     * all Activity implementing NetworkConnectivityListener.
     *
     * @see NetworkState
     * @see NetworkConnectivityListener
     */
    fun Application.registerConnectivityBroadcaster() {
        // Register the Activity Broadcaster
        registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksImp())

        // Get Connectivity Manager
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
            as ConnectivityManager

        // Register to Network Events
        listOf(
            NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build(),
            NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build()
        ).forEach {
            val stateHolder = NetworkStateImp { a, b -> networkEventHandler(a, b) }
            networkStatesSet.add(stateHolder)
            connectivityManager.registerNetworkCallback(it, NetworkCallbackImp(stateHolder))
        }
    }
}
