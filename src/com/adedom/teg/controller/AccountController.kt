package com.adedom.teg.controller

import com.adedom.teg.request.account.*
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.PlayerResponse
import com.adedom.teg.service.teg.TegService
import com.adedom.teg.util.*
import com.adedom.teg.util.jwt.player
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.accountController(service: TegService) {

    put<ImageProfile> {
        val playerId = call.player?.playerId
        val multipart = call.receiveMultipart()
        val response = BaseResponse()
        val message: String? = when {
            playerId == null -> playerId.validateAccessToken()

            else -> {
                val service: BaseResponse = service.changeImageProfile(playerId, multipart)
                response.success = service.success
                service.message
            }
        }
        response.message = message
        call.respond(response)
    }

    get<PlayerInfoRequest> {
        val response = PlayerResponse()
        val playerId = call.player?.playerId
        val message: String? = when {
            playerId == null -> playerId.validateAccessToken()

            else -> {
                val service: PlayerResponse = service.fetchPlayerInfo(playerId)
                response.success = service.success
                response.playerInfo = service.playerInfo
                service.message
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

            request.state.isNullOrBlank() -> StateRequest::state.name.validateIsNullOrBlank()
            !request.state.validateState() -> StateRequest::state.name.validateIncorrect()

            else -> {
                val service: BaseResponse = service.playerState(
                    playerId,
                    StateRequest(request.state)
                )
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

            oldPassword.isNullOrBlank() -> ChangePasswordRequest::oldPassword.name.validateIsNullOrBlank()

            newPassword.isNullOrBlank() -> ChangePasswordRequest::newPassword.name.validateIsNullOrBlank()
            newPassword.length < TegConstant.MIN_PASSWORD -> ChangePasswordRequest::newPassword.name validateGrateEq TegConstant.MIN_PASSWORD

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

    put<ChangeProfileRequest> {
        val response = BaseResponse()
        val (name, gender) = call.receive<ChangeProfileRequest>()
        val playerId = call.player?.playerId
        val message: String? = when {
            playerId == null -> playerId.validateAccessToken()

            name.isNullOrBlank() -> ChangeProfileRequest::name.name.validateIsNullOrBlank()
            name.length < TegConstant.MIN_NAME -> ChangeProfileRequest::name.name.validateGrateEq(TegConstant.MIN_NAME)

            gender.isNullOrBlank() -> ChangeProfileRequest::gender.name.validateIsNullOrBlank()
            !gender.validateGender() -> ChangeProfileRequest::gender.name.validateIncorrect()

            else -> {
                val service: BaseResponse = service.changeProfile(
                    playerId,
                    ChangeProfileRequest(name, gender)
                )
                response.success = service.success
                service.message
            }
        }
        response.message = message
        call.respond(response)
    }

}
