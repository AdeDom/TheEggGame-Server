package com.adedom.teg.models.report.four

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/report/item-collection-history/{filter}")
data class ItemCollectionHistoryRequest(
    val filter: String? = null,
    val begin: Long? = null,
    val end: Long? = null,
)
