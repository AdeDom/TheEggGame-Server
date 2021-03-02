package com.adedom.teg.data.models

data class LogActiveDb(
    val logId: Int,
    val playerId: String,
    val dateTimeIn: Long,
    val dateTimeOut: Long?,
)
