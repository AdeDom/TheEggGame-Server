package com.adedom.teg.models.report

data class MultiItem(
    val multiId: Int,
    val roomNo: String,
    val latitude: Double,
    val longitude: Double,
    val status: String,
    val dateTimeCreated: Long,
    val dateTimeUpdated: Long?,
)
