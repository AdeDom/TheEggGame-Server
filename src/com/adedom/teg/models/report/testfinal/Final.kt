package com.adedom.teg.models.report.testfinal

data class Final(
    val playerId: String? = null,
    val name: String? = null,
    val totalDate: Int? = null,
    val subTotalScore: Int? = null,
    val `data`: List<Data> = emptyList(),
)
