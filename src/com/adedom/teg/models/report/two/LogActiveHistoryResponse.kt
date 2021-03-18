package com.adedom.teg.models.report.two

data class LogActiveHistoryResponse(
    var peopleAll: Int? = null,
    var grandTotalTimePeriod: String? = null,
    var logActiveHistories: List<LogActiveHistory> = emptyList(),
)
