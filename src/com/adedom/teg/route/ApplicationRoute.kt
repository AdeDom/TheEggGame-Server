package com.adedom.teg.route

import com.adedom.teg.request.PostLogActive
import com.adedom.teg.request.PutLogActive
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.RankResponse
import com.adedom.teg.transaction.DatabaseTransaction
import com.adedom.teg.util.validateEmpty
import com.adedom.teg.util.validateIncorrect
import com.adedom.teg.util.validateLessEqZero
import com.adedom.teg.util.validateNotFound
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

fun Route.getPlayers() {

    route("players") {
        val searchKey = "search"
        val limitKey = "limit"
        get("fetch-players") {
            val response = RankResponse()
            val search = call.parameters[searchKey]
            val limit = call.parameters[limitKey]
            val convertLimit = if (limit.isNullOrBlank()) 0 else limit.toInt()
            val message = when {
                search == null -> searchKey.validateEmpty()

                limit == null -> limitKey.validateEmpty()

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

fun Route.putLogActive() {

    route("log-active") {
        post("/") {
            val response = BaseResponse()
            val (logKey, playerId) = call.receive<PostLogActive>()
            val message = when {
                logKey.isNullOrBlank() -> PostLogActive::logKey.name.validateEmpty()
                logKey.length != 32 -> PostLogActive::logKey.name.validateIncorrect()
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
