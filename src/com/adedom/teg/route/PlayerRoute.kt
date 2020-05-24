package com.adedom.teg.route

import com.adedom.teg.request.PutPassword
import com.adedom.teg.request.PutState
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.PlayerResponse
import com.adedom.teg.transaction.DatabaseTransaction
import com.adedom.teg.util.validateEmpty
import com.adedom.teg.util.validateGrateEq
import com.adedom.teg.util.validateLessEqZero
import com.adedom.teg.util.validateNotFound
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.route

fun Route.getPlayer() {

    route("player") {
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
                        val player = DatabaseTransaction.getPlayer(playerId.toInt())
                        response.success = true
                        response.player = player
                        "Fetch player success"
                    }
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}

fun Route.putPassword() {

    route("password") {
        put("/") {
            val response = BaseResponse()
            val (playerId, oldPassword, newPassword) = call.receive<PutPassword>()
            val minPassword = 4
            val message = when {
                playerId == null -> PutPassword::playerId.name.validateEmpty()
                playerId <= 0 -> PutPassword::playerId.name.validateLessEqZero()
                DatabaseTransaction.getCountPlayer(playerId) == 0 -> PutPassword::playerId.name.validateNotFound()

                oldPassword.isNullOrBlank() -> PutPassword::oldPassword.name.validateEmpty()
                DatabaseTransaction.getCountPasswordPlayer(
                    putPassword = PutPassword(
                        playerId = playerId,
                        oldPassword = oldPassword
                    )
                ) == 0 -> PutPassword::oldPassword.name.validateLessEqZero()

                newPassword.isNullOrBlank() -> PutPassword::newPassword.name.validateEmpty()
                newPassword.length < minPassword -> PutPassword::newPassword.name validateGrateEq minPassword

                else -> {
                    DatabaseTransaction.putPassword(
                        putPassword = PutPassword(
                            playerId = playerId,
                            newPassword = newPassword
                        )
                    )
                    response.success = true
                    "Put password success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}

fun Route.putState() {

    route("state") {
        put("/") {
            val response = BaseResponse()
            val (playerId, state) = call.receive<PutState>()
            val message = when {
                playerId == null -> PutState::playerId.name.validateEmpty()
                playerId <= 0 -> PutState::playerId.name.validateLessEqZero()
                DatabaseTransaction.getCountPlayer(playerId) == 0 -> PutState::playerId.name.validateNotFound()

                state.isNullOrBlank() -> PutState::state.name.validateEmpty()

                else -> {
                    DatabaseTransaction.putState(
                        putState = PutState(
                            playerId = playerId,
                            state = state
                        )
                    )
                    response.success = true
                    "Put state success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}
