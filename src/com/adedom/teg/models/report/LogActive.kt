package com.adedom.teg.models.report

data class LogActive(
    val logId: Int,
    val playerId: String,
    val dateTimeIn: Long,
    val dateTimeOut: Long?,
)
