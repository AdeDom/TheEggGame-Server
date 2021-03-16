package com.adedom.teg.models.report.testfinal

data class Data(
    val dataId: String? = null,
    val branchTotalScore: Int? = null,
    val subData: List<SubData> = emptyList(),
)
