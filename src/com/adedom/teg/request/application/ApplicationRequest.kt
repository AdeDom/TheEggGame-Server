package com.adedom.teg.request.application

import io.ktor.locations.*

@Location("/api/application/log-active")
data class LogActiveRequest(val flagLogActive: Int? = null)
