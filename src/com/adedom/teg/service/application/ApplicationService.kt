package com.adedom.teg.service.application

import com.adedom.teg.controller.application.model.RankPlayersRequest
import com.adedom.teg.request.application.LogActiveRequest
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.RankPlayersResponse

interface ApplicationService {

    fun fetchRankPlayers(rankPlayersRequest: RankPlayersRequest): RankPlayersResponse

    fun postLogActive(playerId: String, logActiveRequest: LogActiveRequest): BaseResponse

}
