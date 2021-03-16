package com.adedom.teg.models.report.testfinal

data class FinalResponse(
    var success: Boolean = false,
    var message: String? = null,
    var grandTotalPeople: Int? = null,
    var grandTotalScore: Int? = null,
    var finals: List<Final> = emptyList(),
)
