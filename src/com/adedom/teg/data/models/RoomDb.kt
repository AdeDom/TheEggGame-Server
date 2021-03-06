package com.adedom.teg.data.models

data class RoomDb(
    val roomId: Int,
    val roomNo: String,
    val name: String,
    val people: Int,
    val status: String,
    val startTime: Long?,
    val endTime: Long?,
    val dateTime: Long,
)
