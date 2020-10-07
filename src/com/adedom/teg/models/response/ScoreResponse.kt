package com.adedom.teg.models.response

import com.adedom.teg.data.models.ScoreDb

data class ScoreResponse(
    var success: Boolean = false,
    var message: String? = null,
    var score: ScoreDb? = null,
)
