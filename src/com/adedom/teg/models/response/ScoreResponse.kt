package com.adedom.teg.models.response

import com.adedom.teg.refactor.Score

data class ScoreResponse(
    var success: Boolean = false,
    var message: String? = null,
    var score: Score? = null,
)
