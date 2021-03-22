package com.adedom.teg.models.report.five

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/report/multi-collection-history/{filter}")
data class MultiCollectionHistoryRequest(
    val filter: String? = null,
    val begin: Long? = null,
    val end: Long? = null,
)
