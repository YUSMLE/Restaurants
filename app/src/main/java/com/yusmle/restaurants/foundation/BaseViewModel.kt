package com.yusmle.restaurants.foundation

import android.annotation.SuppressLint
import androidx.core.util.Preconditions
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusmle.restaurants.common.AndroidDisposable
import com.yusmle.restaurants.common.DispatcherProvider
import com.yusmle.restaurants.common.DispatcherProviderImpl
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

open class BaseViewModel(
    val dispatcherProvider: DispatcherProvider = DispatcherProviderImpl()
) : ViewModel() {

    suspend inline fun <T> onUI(crossinline coroutine: suspend () -> T): T {
        return withContext(dispatcherProvider.ui()) {
            coroutine()
        }
    }

    suspend inline fun <T> onIO(crossinline coroutine: suspend () -> T): T {
        return withContext(dispatcherProvider.io()) {
            coroutine()
        }
    }

    suspend inline fun <T> onBG(crossinline coroutine: suspend () -> T): T {
        return withContext(dispatcherProvider.bg()) {
            coroutine()
        }
    }

    protected fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return viewModelScope.launch(context, start, block)
    }

    /**
     * To be a RX-friendly ViewModel!
     */
    private var androidDisposable = AndroidDisposable()

    /**
     * Add disposable to current [io.reactivex.disposables.CompositeDisposable].
     */
    @SuppressLint("RestrictedApi")
    protected fun addDisposable(disposable: Disposable) {
        Preconditions.checkNotNull(disposable)
        androidDisposable.add(disposable)
    }

    /**
     * Dispose from current [io.reactivex.disposables.CompositeDisposable].
     */
    @SuppressLint("RestrictedApi")
    protected fun dispose() {
        Preconditions.checkNotNull(androidDisposable)
        if (androidDisposable.isDisposed.not()) {
            androidDisposable.dispose()
        }
    }

    override fun onCleared() {
        dispose()
        super.onCleared()
    }
}
