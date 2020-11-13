package com.adedom.teg.http.controller

import com.adedom.teg.business.application.ApplicationService
import com.adedom.teg.models.request.*
import com.adedom.teg.util.playerId
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

    // on == 1
    post<LogActiveRequest> {
        val response = service.logActiveOn(call.playerId)
        call.respond(response)
    }

    // off == 0
    patch<LogActiveRequest> {
        val response = service.logActiveOff(call.playerId)
        call.respond(response)
    }

    get<MissionInfoRequest> {
        val response = service.fetchMissionMain(call.playerId)
        call.respond(response)
    }

    post<MissionRequest> {
        val request = call.receive<MissionRequest>()
        val response = service.missionMain(call.playerId, request)
        call.respond(response)
    }

    patch<ChangeCurrentModeRequest> {
        val response = service.changeCurrentMode(call.playerId, it)
        call.respond(response)
    }

}
