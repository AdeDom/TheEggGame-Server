package com.adedom.teg.request.single

import io.ktor.locations.Location

@Location("/api/single/item-collection")
data class ItemCollectionRequest(
    val itemId: Int? = null,
    val qty: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
