package com.adedom.teg.controller

import com.adedom.teg.request.application.RankPlayers
import com.adedom.teg.response.RankPlayersResponse
import com.adedom.teg.service.TegService
import com.adedom.teg.transaction.DatabaseTransaction
import com.adedom.teg.util.jwt.player
import com.adedom.teg.util.validateAccessToken
import com.adedom.teg.util.validateEmpty
import com.adedom.teg.util.validateIncorrect
import io.ktor.application.call
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

fun Route.applicationController(service: TegService) {

    get<RankPlayers> { request ->
        val response = RankPlayersResponse()
        val playerId = call.player?.playerId
        val search: String? = request.search
        val limit: Int = if (request.limit.isNullOrBlank()) 0 else request.limit.toInt()
        val message = when {
            playerId == null -> playerId.validateAccessToken()

            search == null -> RankPlayers::search.name.validateEmpty()

            limit <= 0 -> RankPlayers::limit.name.validateIncorrect()

            else -> {
                val players = DatabaseTransaction.getPlayers(search, limit)
                response.rankPlayers = players
                response.success = true
                "Fetch players success"
            }
        }
        response.message = message
        call.respond(response)
    }

}
