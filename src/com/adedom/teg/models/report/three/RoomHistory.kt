package com.adedom.teg.models.report.three

data class RoomHistory(
    val roomId: Int? = null,
    val roomNo: String? = null,
    val name: String? = null,
    val people: Int? = null,
    val status: String? = null,
    val dateTime: String? = null,
    val peopleAll: Int? = null,
    val roomInfoHistories: List<RoomInfoHistory> = emptyList(),
)
