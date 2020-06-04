package com.adedom.teg.response

data class MultiScoreResponse(
    var teamA: Int? = null,
    var teamB: Int? = null
) : BaseResponse()
