package com.adedom.teg.business.service.application

import com.adedom.teg.http.models.request.RankPlayersRequest
import com.adedom.teg.http.models.response.BaseResponse
import com.adedom.teg.http.models.response.RankPlayersResponse
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
interface ApplicationService {

    fun fetchRankPlayers(rankPlayersRequest: RankPlayersRequest): RankPlayersResponse

    fun logActiveOn(playerId: String?): BaseResponse

    fun logActiveOff(playerId: String?): BaseResponse

}
