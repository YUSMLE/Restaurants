package com.yusmle.network.connectivity

import androidx.lifecycle.LiveData

/**
 * This liveData enabling network connectivity monitoring
 *
 * @see ConnectivityStateHolder to get current connection state
 */
object NetworkEvents : LiveData<Event>() {

    internal fun notify(event: Event) {
        postValue(event)
    }
}
