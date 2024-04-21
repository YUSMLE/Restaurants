package com.yusmle.restaurants.common.foundation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.yusmle.restaurants.common.repeatOnLifecycleResumedX
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicBoolean

abstract class StatefulIntentViewModel<Intent, State>(
    initialState: State
) : IntentViewModel<Intent>() {

    private var isViewModelInitialized: AtomicBoolean = AtomicBoolean(false)

    private val stateMutex = Mutex()

    private val _states = MutableStateFlow(initialState)

    val states: StateFlow<State>
        get() = _states

    val currentState: State
        get() = _states.value

    protected suspend fun setState(reducer: State.() -> State) =
        stateMutex.withLock {
            _states.value = _states.value.reducer()
        }

    protected suspend fun withState(action: (State).() -> Unit) =
        setState {
            this.apply(action)
        }

    protected open fun onInitialize() {
        /* Nothing by default */
    }

    fun observeX(
        lifecycleOwner: LifecycleOwner,
        callback: (input: State) -> Unit
    ) {
        if (isViewModelInitialized.get().not()) {
            onInitialize()
            isViewModelInitialized.set(true)
        }

        lifecycleOwner.lifecycleScope.repeatOnLifecycleResumedX(lifecycleOwner) {
            _states.collect {
                callback(it)
            }
        }
    }
}
