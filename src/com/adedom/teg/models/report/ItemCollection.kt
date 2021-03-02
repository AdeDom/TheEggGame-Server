package com.adedom.teg.models.report

data class ItemCollection(
    val collectionId: Int,
    val playerId: String,
    val itemId: Int,
    val qty: Int,
    val latitude: Double,
    val longitude: Double,
    val dateTime: Long,
    val mode: String,
)
