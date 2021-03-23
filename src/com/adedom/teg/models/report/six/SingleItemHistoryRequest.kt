package com.adedom.teg.models.report.six

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/report/single-item-history/{filter}")
data class SingleItemHistoryRequest(
    val filter: String? = null,
    val begin: Long? = null,
    val end: Long? = null,
)
