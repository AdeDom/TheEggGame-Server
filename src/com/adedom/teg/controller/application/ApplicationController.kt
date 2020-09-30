package com.adedom.teg.controller.application

import com.adedom.teg.controller.application.model.RankPlayersRequest
import com.adedom.teg.request.application.LogActiveRequest
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.service.application.ApplicationService
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

@KtorExperimentalLocationsAPI
fun Route.applicationController(service: ApplicationService) {

    get<RankPlayersRequest> {
        val response = service.fetchRankPlayers(it)
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
