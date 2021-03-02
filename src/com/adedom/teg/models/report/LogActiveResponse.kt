package com.adedom.teg.models.report

data class LogActiveResponse(
    var success: Boolean = false,
    var message: String? = null,
    var logActives: List<LogActive> = emptyList(),
)
