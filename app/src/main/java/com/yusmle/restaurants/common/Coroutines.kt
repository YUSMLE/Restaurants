package com.yusmle.restaurants.common

import androidx.lifecycle.*
import com.yusmle.restaurants.common.annotation.Warning
import kotlinx.coroutines.*

interface DispatcherProvider {
    fun ui(): CoroutineDispatcher
    fun io(): CoroutineDispatcher
    fun bg(): CoroutineDispatcher
}

class DispatcherProviderImpl : DispatcherProvider {
    override fun ui(): CoroutineDispatcher = Dispatchers.Main
    override fun io(): CoroutineDispatcher = Dispatchers.IO
    override fun bg(): CoroutineDispatcher = Dispatchers.Default
}

suspend inline fun <T> CoroutineScope.onUI(crossinline block: suspend () -> T): T {
    return withContext(Dispatchers.Main) {
        block()
    }
}

suspend inline fun <T> CoroutineScope.onIO(crossinline block: suspend () -> T): T {
    return withContext(Dispatchers.IO) {
        block()
    }
}

suspend inline fun <T> CoroutineScope.onBG(crossinline block: suspend () -> T): T {
    return withContext(Dispatchers.Default) {
        block()
    }
}

/**
 * The block passed to repeatOnLifecycle is executed when the lifecycle
 * is at least RESUMED and is cancelled when the lifecycle is PAUSED.
 * It automatically restarts the block when the lifecycle is RESUMED again.
 */
@Warning("Experimental Top Level Function")
fun CoroutineScope.repeatOnLifecycleResumedX(
    lifecycleOwner: LifecycleOwner,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            launch(block = block)
        }
    }
}
