package com.adedom.teg.models.report.two

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/report/log-active-history/{filter}")
data class LogActiveHistoryRequest(
    val filter: String? = null,
    val dateIn: Long? = null,
    val timeIn: Long? = null,
    val dateOut: Long? = null,
    val timeOut: Long? = null,
)
