package com.adedom.teg.models

import org.joda.time.DateTime

data class LogActive(
    val logId: Int? = null,
    val logKey: String? = null,
    val playerId: Int? = null,
    val dateTimeIn: DateTime? = null,
    val dateTimeOut: DateTime? = null
)
