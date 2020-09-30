package com.adedom.teg.controller.single.model

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/single/item-collection")
data class ItemCollectionRequest(
    val itemId: Int? = null,
    val qty: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
