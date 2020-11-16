package com.adedom.teg.models.request

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/single/item-collection/{singleId}")
data class SingleItemRequest(
    val singleId: Int? = null,
)
