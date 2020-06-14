package com.adedom.teg.route

import com.adedom.teg.request.LogActiveRequest
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.RankResponse
import com.adedom.teg.transaction.DatabaseTransaction
import com.adedom.teg.util.*
import com.adedom.teg.util.jwt.player
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

fun Route.getPlayers() {

    route("players") {
        get {
            val response = RankResponse()
            val playerId = call.player?.playerId
            val search = call.parameters[GetConstant.SEARCH]
            val limit = call.parameters[GetConstant.LIMIT]
            val convertLimit = if (limit.isNullOrBlank()) 0 else limit.toInt()
            val message = when {
                playerId == null -> playerId.validateAccessToken()

                search == null -> GetConstant.SEARCH.validateEmpty()

                limit == null -> GetConstant.LIMIT.validateEmpty()

                else -> {
                    val players = DatabaseTransaction.getPlayers(search, convertLimit)
                    response.players = players
                    response.success = true
                    "Fetch players success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}

fun Route.logActive() {

    route("log-active") {
        post("/") {
            val response = BaseResponse()
            val (logKey) = call.receive<LogActiveRequest>()
            val playerId = call.player?.playerId
            val message = when {
                playerId == null -> playerId.validateAccessToken()

                logKey.isNullOrBlank() -> LogActiveRequest::logKey.name.validateEmpty()
                logKey.length != CommonConstant.LOGS_KEYS -> LogActiveRequest::logKey.name.validateIncorrect()
                !DatabaseTransaction.validateLogActive(logKey) -> LogActiveRequest::logKey.name.validateIncorrect()

                else -> {
                    DatabaseTransaction.postLogActive(playerId, logKey)
                    response.success = true
                    "Post log active success"
                }
            }
            response.message = message
            call.respond(response)
        }

        patch("/") {
            val response = BaseResponse()
            val (logKey) = call.receive<LogActiveRequest>()
            val playerId = call.player?.playerId
            val message = when {
                playerId == null -> playerId.validateAccessToken()

                logKey.isNullOrBlank() -> LogActiveRequest::logKey.name.validateEmpty()
                DatabaseTransaction.validateLogActive(logKey) -> LogActiveRequest::logKey.name.validateNotFound()

                else -> {
                    DatabaseTransaction.patchLogActive(logKey)
                    response.success = true
                    "Put log active success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}
