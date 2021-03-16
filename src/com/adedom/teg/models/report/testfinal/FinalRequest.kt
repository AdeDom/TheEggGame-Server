package com.adedom.teg.models.report.testfinal

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/api/report/test-final/{pantip}")
data class FinalRequest(
    val pantip: String? = null,
    val begin: Long? = null,
    val end: Long? = null,
)
