package com.adedom.teg.controller

import com.adedom.teg.request.application.RankPlayers
import com.adedom.teg.service.TegService
import io.ktor.application.call
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

fun Route.applicationController(service: TegService) {

    get<RankPlayers> { request ->
        val rankPlayers: RankPlayers = request
        call.respond(rankPlayers)
    }

}
