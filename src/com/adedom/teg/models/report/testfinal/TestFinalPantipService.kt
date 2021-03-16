package com.adedom.teg.models.report.testfinal

data class TestFinalPantipService(
    val collectionId: Int,
    val playerId: String,
    val itemId: Int,
    val qty: Int,
    val dateTimeLong: Long,
    val dateString: String,
    val timeString: String,
    val mode: String,
)
