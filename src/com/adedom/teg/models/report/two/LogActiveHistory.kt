package com.adedom.teg.models.report.two

data class LogActiveHistory(
    val playerId: String? = null,
    val name: String? = null,
    val time: Int? = null,
    val totalTimePeriod: String? = null,
    val logActiveHistoryDataList: List<LogActiveHistoryData> = emptyList(),
)
