package com.adedom.teg.models.report.two

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/report/log-active-history/{filter}")
data class LogActiveHistoryRequest(
    val filter: String? = null,
    val dateTimeIn: Long? = null,
    val dateTimeOut: Long? = null,
)
