package com.adedom.teg.route

import com.adedom.teg.request.SetState
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.PlayerResponse
import com.adedom.teg.transaction.DatabaseTransaction
import com.adedom.teg.util.validateEmpty
import com.adedom.teg.util.validateLessEqZero
import com.adedom.teg.util.validateNotFound
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.route

fun Route.user() {

    val baseResponse = BaseResponse()

    route("get-player") {
        val playerIdKey = "player_id"
        get("/{$playerIdKey}") {
            val response = PlayerResponse()
            val playerId = call.parameters[playerIdKey]
            val message = when {
                playerId.isNullOrBlank() -> playerIdKey.validateEmpty()
                playerId.toInt() <= 0 -> playerIdKey.validateLessEqZero()
                else -> {
                    val count = DatabaseTransaction.getCountPlayer(playerId.toInt())
                    if (count == 0) {
                        playerIdKey.validateNotFound()
                    } else {
                        val player = DatabaseTransaction.getPlayerInfo(playerId.toInt())
                        response.success = true
                        response.player = player
                        "Fetch player"
                    }
                }
            }
            response.message = message
            call.respond(response)
        }
    }

    route("set-state") {
        put("/") {
            val (playerId, state) = call.receive<SetState>()
            val message = when {
                playerId == null -> SetState::playerId.name.validateEmpty()
                playerId <= 0 -> SetState::playerId.name.validateLessEqZero()
                DatabaseTransaction.getCountPlayer(playerId) == 0 -> SetState::playerId.name.validateNotFound()

                state.isNullOrBlank() -> SetState::state.name.validateEmpty()

                else -> {
                    DatabaseTransaction.putSetState(
                        setState = SetState(
                            playerId = playerId,
                            state = state
                        )
                    )
                    baseResponse.success = true
                    "Set state success"
                }
            }
            baseResponse.message = message
            call.respond(baseResponse)
        }
    }

}
