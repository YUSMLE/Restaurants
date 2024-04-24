package com.yusmle.network.connectivity

sealed class Event {

    class ConnectivityEvent(val isConnected: Boolean) : Event()
}
