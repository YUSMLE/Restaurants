package com.yusmle.restaurants.common

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * [CompositeDisposable] is can not reuse when already disposed.
 * If you want sync disposable lifecycle with Android Activity lifecycle,
 * it is possible to correspond by making simple wrapper.
 *
 * For original solution see following link:
 * {https://dev.to/ryugoo/how-to-make-reusable-compositedisposable-and-kotlin-extension-1db7}
 */
class AndroidDisposable {

    private var compositeDisposable: CompositeDisposable? = null

    fun add(disposable: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }

        compositeDisposable!!.add(disposable)
    }

    fun dispose() {
        compositeDisposable?.let {
            it.dispose()
            compositeDisposable = null
        }
    }

    val isDisposed: Boolean
        get() = compositeDisposable?.isDisposed ?: true
}
