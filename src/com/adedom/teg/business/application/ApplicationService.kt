package com.adedom.teg.business.application

import com.adedom.teg.models.request.ChangeCurrentModeRequest
import com.adedom.teg.models.request.MissionRequest
import com.adedom.teg.models.response.BaseResponse
import com.adedom.teg.models.response.MissionResponse
import com.adedom.teg.models.response.RankPlayersResponse
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
internal interface ApplicationService {

    fun fetchRankPlayers(): RankPlayersResponse

    fun logActiveOn(playerId: String?): BaseResponse

    fun logActiveOff(playerId: String?): BaseResponse

    fun fetchMissionMain(playerId: String?): MissionResponse

    fun missionMain(playerId: String?, missionRequest: MissionRequest): BaseResponse

    fun changeCurrentMode(playerId: String?, changeCurrentModeRequest: ChangeCurrentModeRequest): BaseResponse

}
