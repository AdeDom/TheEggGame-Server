package com.adedom.teg.service.application

import com.adedom.teg.controller.application.model.RankPlayersRequest
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.RankPlayersResponse
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
interface ApplicationService {

    fun fetchRankPlayers(rankPlayersRequest: RankPlayersRequest): RankPlayersResponse

    fun logActiveOn(playerId: String?): BaseResponse

    fun logActiveOff(playerId: String?): BaseResponse

}
