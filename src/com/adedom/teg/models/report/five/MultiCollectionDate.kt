package com.adedom.teg.models.report.five

data class MultiCollectionDate(
    val dateId: String? = null,
    val date: String? = null,
    val timeAll: Int? = null,
    val multiCollectionTimes: List<MultiCollectionTime> = emptyList(),
)
