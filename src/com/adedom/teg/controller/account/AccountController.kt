package com.adedom.teg.controller.account

import com.adedom.teg.controller.account.model.ChangePasswordRequest
import com.adedom.teg.controller.account.model.ChangeProfileRequest
import com.adedom.teg.controller.account.model.PlayerInfoRequest
import com.adedom.teg.controller.account.model.StateRequest
import com.adedom.teg.request.account.ImageProfile
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.service.account.AccountService
import com.adedom.teg.util.jwt.player
import com.adedom.teg.util.jwt.playerId
import com.adedom.teg.util.validateAccessToken
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
        val request = call.receive<ChangeProfileRequest>()
        val response = service.changeProfile(call.playerId, request)
        call.respond(response)
    }

}
