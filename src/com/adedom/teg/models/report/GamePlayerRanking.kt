package com.adedom.teg.models.report

data class GamePlayerRanking(
    val playerId: String,
    val name: String,
    val image: String?,
    val gender: String,
    val birthDateLong: Long,
    val birthDateString: String,
    val state: String?,
    val latitude: Double?,
    val longitude: Double?,
    val currentMode: String?,
    val dateTimeCreated: String,
    val dateTimeUpdated: String?,
    val level: Int,
)
