package com.adedom.teg.models.report.six

data class MultiItemHistoryDb(
    val singleId: Int,
    val itemTypeId: Int,
    val playerId: String?,
    val name: String?,
    val status: String,
    val dateTimeCreated: Long,
    val dateCreated: String,
    val timeCreated: String,
    val dateTimeUpdated: String?,
)
