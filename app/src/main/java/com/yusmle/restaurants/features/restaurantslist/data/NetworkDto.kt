package com.yusmle.restaurants.features.restaurantslist.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.yusmle.restaurants.common.helper.util.emptyString

/**
 * Network DTOs / Models here
 */

@JsonClass(generateAdapter = true)
data class RestaurantsSearchBundleResult(
    @Json(name = "paging_meta")
    val pagingMeta: PagingMetaResult,

    @Json(name = "results")
    val results: List<RestaurantResult>
)

@JsonClass(generateAdapter = true)
data class PagingMetaResult(
    @Json(name = "has_next_page")
    val hasNextPage: Boolean,

    @Json(name = "data")
    val data: String?
)

@JsonClass(generateAdapter = true)
data class RestaurantResult(
    @Json(name = "name")
    val name: String,

    @Json(name = "phone_number")
    val phoneNumber: String?,

    @Json(name = "review_rate")
    val reviewRate: Float,

    @Json(name = "address")
    val address: String = emptyString(), // Default value for items without having [address] set

    @Json(name = "distance")
    val distance: String
)
