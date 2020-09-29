package com.adedom.teg.controller.account

import com.adedom.teg.controller.account.model.ChangePasswordRequest
import com.adedom.teg.controller.account.model.PlayerInfoRequest
import com.adedom.teg.controller.account.model.StateRequest
import com.adedom.teg.request.account.ChangeProfileRequest
import com.adedom.teg.request.account.ImageProfile
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.service.account.AccountService
import com.adedom.teg.util.*
import com.adedom.teg.util.jwt.player
import com.adedom.teg.util.jwt.playerId
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

@KtorExperimentalLocationsAPI
fun Route.accountController(service: AccountService) {

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
        val response = service.fetchPlayerInfo(call.playerId)
        call.respond(response)
    }

    patch<StateRequest> {
        val response = service.playerState(call.playerId, it)
        call.respond(response)
    }

    put<ChangePasswordRequest> {
        val request = call.receive<ChangePasswordRequest>()
        val response = service.changePassword(call.playerId, request)
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
