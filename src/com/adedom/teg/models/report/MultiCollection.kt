package com.adedom.teg.models.report

data class MultiCollection(
    val collectionId: Int,
    val roomNo: String,
    val playerId: String,
    val team: String,
    val latitude: Double,
    val longitude: Double,
    val dateTime: Long,
)
