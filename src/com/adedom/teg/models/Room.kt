package com.adedom.teg.models

import org.joda.time.DateTime

data class Room(
    val roomId: Int? = null,
    val roomNo: String? = null,
    val name: String? = null,
    val people: String? = null,
    val status: String? = null,
    val dateTime: DateTime? = null
)
