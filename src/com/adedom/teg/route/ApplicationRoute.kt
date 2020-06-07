package com.adedom.teg.route

import com.adedom.teg.request.PostLogActive
import com.adedom.teg.request.PutLogActive
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.RankResponse
import com.adedom.teg.transaction.DatabaseTransaction
import com.adedom.teg.util.*
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

fun Route.getPlayers() {

    route("players") {
        get("fetch-players") {
            val response = RankResponse()
            val search = call.parameters[GetConstant.SEARCH]
            val limit = call.parameters[GetConstant.LIMIT]
            val convertLimit = if (limit.isNullOrBlank()) 0 else limit.toInt()
            val message = when {
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
            val (logKey, playerId) = call.receive<PostLogActive>()
            val message = when {
                logKey.isNullOrBlank() -> PostLogActive::logKey.name.validateEmpty()
                logKey.length != CommonConstant.LOGS_KEYS -> PostLogActive::logKey.name.validateIncorrect()
                DatabaseTransaction.getCountLogActive(logKey) != 0 -> PostLogActive::logKey.name.validateIncorrect()

                playerId == null -> PostLogActive::playerId.name.validateEmpty()
                playerId <= 0 -> PostLogActive::playerId.name.validateLessEqZero()
                DatabaseTransaction.getCountPlayer(playerId) == 0 -> PostLogActive::playerId.name.validateNotFound()

                else -> {
                    DatabaseTransaction.postLogActive(
                        postLogActive = PostLogActive(
                            logKey = logKey,
                            playerId = playerId
                        )
                    )
                    response.success = true
                    "Post log active success"
                }
            }
            response.message = message
            call.respond(response)
        }

        put("/") {
            val response = BaseResponse()
            val (logKey) = call.receive<PutLogActive>()
            val message = when {
                logKey.isNullOrBlank() -> PutLogActive::logKey.name.validateEmpty()
                DatabaseTransaction.getCountLogActive(logKey) == 0 -> PutLogActive::logKey.name.validateNotFound()

                else -> {
                    DatabaseTransaction.putLogActive(logKey)
                    response.success = true
                    "Put log active success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}
