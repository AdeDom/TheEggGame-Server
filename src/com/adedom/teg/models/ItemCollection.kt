package com.adedom.teg.models

import org.joda.time.DateTime

data class ItemCollection(
    val collectionId: Int? = null,
    val playerId: Int? = null,
    val itemId: Int? = null,
    val qty: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val dateTime: DateTime? = null
)
