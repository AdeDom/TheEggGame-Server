package com.adedom.teg.data.models

data class MultiItemDb(
    val multiId: Int,
    val roomNo: String,
    val latitude: Double,
    val longitude: Double,
    val status: String,
    val dateTimeCreated: Long,
    val dateTimeUpdated: Long?,
)
