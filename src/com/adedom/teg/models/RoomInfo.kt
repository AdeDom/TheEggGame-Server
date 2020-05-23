package com.adedom.teg.models

import org.joda.time.DateTime

data class RoomInfo(
    val infoId: Int? = null,
    val roomNo: String? = null,
    val playerId: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val team: String? = null,
    val status: String? = null,
    val dateTime: DateTime? = null
)
