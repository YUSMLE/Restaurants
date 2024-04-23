package com.yusmle.restaurants.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.yusmle.restaurants.common.annotation.Warning
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Repeats collection of Flow when the lifecycle is RESUMED, cancels when PAUSED.
 */
@Warning("Experimental Top Level Function")
fun <T> Flow<T>.observeX(lifecycleOwner: LifecycleOwner, callback: (input: T) -> Unit): Job {
    return lifecycleOwner.lifecycleScope.launch {
        flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
            .collect {
                callback(it)
            }
    }
}
