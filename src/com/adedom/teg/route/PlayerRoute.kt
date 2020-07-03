package com.adedom.teg.route

import com.adedom.teg.request.PasswordRequest
import com.adedom.teg.request.ProfileRequest
import com.adedom.teg.request.StateRequest
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.PlayerResponse
import com.adedom.teg.transaction.DatabaseTransaction
import com.adedom.teg.util.*
import com.adedom.teg.util.jwt.player
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

fun Route.getPlayer() {

    route("player") {
        get("/") {
            val response = PlayerResponse()
            val playerId = call.player?.playerId
            val message = when {
                playerId == null -> playerId.validateAccessToken()

                else -> {
                    val player = DatabaseTransaction.getPlayer(playerId)
                    response.success = true
                    response.player = player
                    "Fetch player success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}

fun Route.patchPassword() {

    route("password") {
        patch("/") {
            val response = BaseResponse()
            val (oldPassword, newPassword) = call.receive<PasswordRequest>()
            val playerId = call.player?.playerId
            val message = when {
                playerId == null -> playerId.validateAccessToken()

                oldPassword.isNullOrBlank() -> PasswordRequest::oldPassword.name.validateEmpty()
                DatabaseTransaction.validatePasswordPlayer(
                    playerId,
                    oldPassword
                ) -> PasswordRequest::oldPassword.name.validateLessEqZero()

                newPassword.isNullOrBlank() -> PasswordRequest::newPassword.name.validateEmpty()
                newPassword.length < CommonConstant.MIN_PASSWORD -> PasswordRequest::newPassword.name validateGrateEq CommonConstant.MIN_PASSWORD

                else -> {
                    DatabaseTransaction.patchPassword(playerId, newPassword)
                    response.success = true
                    "Patch password success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}

fun Route.putProfile() {

    route("profile") {
        put("/") {
            val response = BaseResponse()
            val (name, gender) = call.receive<ProfileRequest>()
            val playerId = call.player?.playerId
            val message = when {
                playerId == null -> playerId.validateAccessToken()

                name.isNullOrBlank() -> ProfileRequest::name.name.validateEmpty()
                name.length < CommonConstant.MIN_NAME -> ProfileRequest::name.name.validateGrateEq(CommonConstant.MIN_NAME)

                gender.isNullOrBlank() -> ProfileRequest::gender.name.validateEmpty()
                !gender.validateGender() -> ProfileRequest::gender.name.validateIncorrect()

                else -> {
                    DatabaseTransaction.putProfile(
                        playerId,
                        ProfileRequest(name = name, gender = gender)
                    )
                    response.success = true
                    "Put profile success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}

fun Route.patchState() {

    route("state") {
        patch("/") {
            val response = BaseResponse()
            val (state) = call.receive<StateRequest>()
            val playerId = call.player?.playerId
            val message = when {
                playerId == null -> playerId.validateAccessToken()

                state.isNullOrBlank() -> StateRequest::state.name.validateEmpty()
                !state.validateState() -> StateRequest::state.name.validateIncorrect()

                else -> {
                    DatabaseTransaction.patchState(playerId, state)
                    response.success = true
                    "Patch state success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}
