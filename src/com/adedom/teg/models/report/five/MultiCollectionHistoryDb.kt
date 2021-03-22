package com.adedom.teg.models.report.five

data class MultiCollectionHistoryDb(
    val collectionId: Int,
    val roomNo: String,
    val playerId: String,
    val name: String,
    val team: String,
    val dateTime: Long,
    val date: String,
    val time: String,
)
