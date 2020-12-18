package com.adedom.teg.models.request

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/multi/item-collection")
data class MultiItemCollectionRequest(
    val qty: Int? = null,
)
