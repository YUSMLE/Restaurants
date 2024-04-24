package com.yusmle.network.connectivity.internal

import android.util.Log

/**
 * Just like [runCatching], but without result
 *
 * @see runCatching
 */
internal inline fun <T> T.safeRun(tag: String = "", block: T.() -> Unit) {
    try {
        block()
    }
    catch (e: Throwable) {
        // Ignore, but log it.
        Log.e(tag, e.toString())
    }
}
