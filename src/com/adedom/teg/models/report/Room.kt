package com.adedom.teg.models.report

data class Room(
    val roomId: Int,
    val roomNo: String,
    val name: String,
    val people: Int,
    val status: String,
    val startTime: Long?,
    val endTime: Long?,
    val dateTime: Long,
)
