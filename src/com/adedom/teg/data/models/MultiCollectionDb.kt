package com.adedom.teg.data.models

data class MultiCollectionDb(
    val collectionId: Int,
    val roomNo: String,
    val playerId: String,
    val team: String,
    val latitude: Double,
    val longitude: Double,
    val dateTime: Long,
)
