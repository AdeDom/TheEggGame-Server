package com.adedom.teg.controller

import com.adedom.teg.models.Player
import com.adedom.teg.request.account.ChangePasswordRequest
import com.adedom.teg.request.account.ImageProfile
import com.adedom.teg.request.account.PlayerInfo
import com.adedom.teg.request.account.StateRequest
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.PlayerResponse
import com.adedom.teg.service.TegService
import com.adedom.teg.util.*
import com.adedom.teg.util.jwt.player
import io.ktor.application.call
import io.ktor.locations.get
import io.ktor.locations.patch
import io.ktor.locations.put
import io.ktor.request.receive
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.Route

fun Route.accountController(service: TegService) {

    patch<ImageProfile> {
        val playerId = call.player?.playerId
        val multipart = call.receiveMultipart()
        val response = BaseResponse()
        val message: String = when {
            playerId == null -> playerId.validateAccessToken()

            else -> {
                val pair: Pair<String, ImageProfile?> = service.changeImageProfile(playerId, multipart)
                if (pair.second != null) response.success = true
                pair.first
            }
        }
        response.message = message
        call.respond(response)
    }

    get<PlayerInfo> {
        val response = PlayerResponse()
        val playerId = call.player?.playerId
        val message: String = when {
            playerId == null -> playerId.validateAccessToken()

            else -> {
                val pair: Pair<String, Player?> = service.fetchPlayerInfo(playerId)
                if (pair.second != null) {
                    response.success = true
                    response.playerInfo = pair.second
                }
                pair.first
            }
        }
        response.message = message
        call.respond(response)
    }

    patch<StateRequest> { request ->
        val response = BaseResponse()
        val playerId = call.player?.playerId
        val message = when {
            playerId == null -> playerId.validateAccessToken()

            request.state.isNullOrBlank() -> StateRequest::state.name.validateEmpty()
            !request.state.validateState() -> StateRequest::state.name.validateIncorrect()

            else -> {
                val service: BaseResponse = service.playerState(playerId, request.state)
                response.success = service.success
                service.message
            }
        }
        response.message = message
        call.respond(response)
    }

    put<ChangePasswordRequest> {
        val response = BaseResponse()
        val (oldPassword, newPassword) = call.receive<ChangePasswordRequest>()
        val playerId = call.player?.playerId
        val message: String? = when {
            playerId == null -> playerId.validateAccessToken()

            oldPassword.isNullOrBlank() -> ChangePasswordRequest::oldPassword.name.validateEmpty()

            newPassword.isNullOrBlank() -> ChangePasswordRequest::newPassword.name.validateEmpty()
            newPassword.length < CommonConstant.MIN_PASSWORD -> ChangePasswordRequest::newPassword.name validateGrateEq CommonConstant.MIN_PASSWORD

            else -> {
                val service: BaseResponse = service.changePassword(
                    playerId,
                    ChangePasswordRequest(oldPassword, newPassword)
                )
                response.success = service.success
                service.message
            }
        }
        response.message = message
        call.respond(response)
    }

}
