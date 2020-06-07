package com.adedom.teg.response

import com.adedom.teg.models.Score

data class ScoreResponse(
    var score: Score? = null
) : BaseResponse()
