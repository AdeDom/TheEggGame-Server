package com.adedom.teg.route

import com.adedom.teg.request.application.LogActiveRequest
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.transaction.DatabaseTransaction
import com.adedom.teg.util.jwt.player
import com.adedom.teg.util.validateAccessToken
import com.adedom.teg.util.validateEmpty
import com.adedom.teg.util.validateNotFound
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.patch
import io.ktor.routing.route

fun Route.logActive() {

    route("log-active") {
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
                    "Patch log active success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}
