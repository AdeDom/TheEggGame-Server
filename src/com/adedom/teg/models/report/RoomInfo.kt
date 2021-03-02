package com.adedom.teg.models.report

data class RoomInfo(
    val infoId: Int,
    val roomNo: String,
    val playerId: String,
    val latitude: Double?,
    val longitude: Double?,
    val team: String,
    val status: String,
    val role: String,
    val dateTime: Long,
)
