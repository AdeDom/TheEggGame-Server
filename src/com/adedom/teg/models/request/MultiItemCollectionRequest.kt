package com.adedom.teg.models.request

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/multi/item-collection")
data class MultiItemCollectionRequest(
    val itemId: Int? = null,
    val qty: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
)
