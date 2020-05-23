package com.adedom.teg.models

import org.joda.time.DateTime

data class MultiCollection(
    val collectionId: Int? = null,
    val roomNo: String? = null,
    val playerId: Int? = null,
    val score: Int? = null,
    val team: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val dateTime: DateTime? = null
)
