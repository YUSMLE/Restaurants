package com.yusmle.restaurants.foundation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

abstract class IntentViewModel<Intent> : BaseViewModel() {

    private val _intents = Channel<Intent>()

    init {
        viewModelScope.launch {
            _intents.consumeEach { intent ->
                handleIntent(intent)
            }
        }
    }

    fun sendIntent(intent: Intent) = viewModelScope.launch {
        _intents.send(intent)
    }

    protected abstract suspend fun handleIntent(intent: Intent)
}
