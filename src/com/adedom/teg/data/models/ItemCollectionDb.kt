package com.adedom.teg.data.models

data class ItemCollectionDb(
    val collectionId: Int,
    val playerId: String,
    val itemId: Int,
    val qty: Int,
    val latitude: Double,
    val longitude: Double,
    val dateTime: Long,
    val mode: String,
)
