package com.yusmle.restaurants.common.error

import java.io.IOException

data class NoConnectivityException(
    override val message: String? = null,
    override val cause: Throwable? = null
) : IOException(message, cause)
