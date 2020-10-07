package com.adedom.teg.data.models

data class ItemCollectionDb(
    val collectionId: Int? = null,
    val playerId: String? = null,
    val itemId: Int? = null,
    val qty: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val dateTime: Long? = null,
)
