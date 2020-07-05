package com.adedom.teg.controller

import com.adedom.teg.request.account.ImageProfile
import com.adedom.teg.request.account.ImageProfileV2
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.service.TegService
import com.adedom.teg.util.jwt.player
import com.adedom.teg.util.validateAccessToken
import io.ktor.application.call
import io.ktor.locations.patch
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

    patch<ImageProfileV2> {
        val playerId = call.player?.playerId
        val multipart = call.receiveMultipart()
        val response = BaseResponse()
        val message: String = when {
            playerId == null -> playerId.validateAccessToken()

            else -> {
                val pair: Pair<String, ImageProfileV2?> = service.changeImageProfileV2(playerId, multipart)
                if (pair.second != null) response.success = true
                pair.first
            }
        }
        response.message = message
        call.respond(response)
    }

}
