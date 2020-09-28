package com.adedom.teg.controller

import com.adedom.teg.request.application.LogActiveRequest
import com.adedom.teg.request.application.RankPlayersRequest
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.RankPlayersResponse
import com.adedom.teg.service.teg.TegService
import com.adedom.teg.util.jwt.player
import com.adedom.teg.util.validateAccessToken
import com.adedom.teg.util.validateFlagLogActive
import com.adedom.teg.util.validateIncorrect
import com.adedom.teg.util.validateIsNullOrBlank
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.applicationController(service: TegService) {

    get<RankPlayersRequest> { request ->
        val response = RankPlayersResponse()
        val playerId = call.player?.playerId
        val search: String? = request.search
        val limit: Int = if (request.limit.isNullOrBlank()) 0 else request.limit.toInt()
        val message = when {
            playerId == null -> playerId.validateAccessToken()

            search == null -> RankPlayersRequest::search.name.validateIsNullOrBlank()

            limit <= 0 -> RankPlayersRequest::limit.name.validateIncorrect()

            else -> {
                val service: RankPlayersResponse = service.fetchRankPlayers(request)
                response.success = service.success
                response.rankPlayers = service.rankPlayers
                service.message
            }
        }
        response.message = message
        call.respond(response)
    }

    post<LogActiveRequest> {
        val response = BaseResponse()
        val (flagLogActive) = call.receive<LogActiveRequest>()
        val playerId = call.player?.playerId
        val message: String? = when {
            playerId == null -> playerId.validateAccessToken()

            flagLogActive == null -> it::flagLogActive.name.validateIsNullOrBlank()
            !flagLogActive.validateFlagLogActive() -> it::flagLogActive.name.validateIncorrect()

            else -> {
                val service = service.postLogActive(
                    playerId,
                    LogActiveRequest(flagLogActive)
                )
                response.success = service.success
                service.message
            }
        }
        response.message = message
        call.respond(response)
    }

}
