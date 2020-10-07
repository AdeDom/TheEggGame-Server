package com.adedom.teg.business.application

import com.adedom.teg.models.request.RankPlayersRequest
import com.adedom.teg.models.response.BaseResponse
import com.adedom.teg.models.response.RankPlayersResponse
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
interface ApplicationService {

    fun fetchRankPlayers(rankPlayersRequest: RankPlayersRequest): RankPlayersResponse

    fun logActiveOn(playerId: String?): BaseResponse

    fun logActiveOff(playerId: String?): BaseResponse

}
