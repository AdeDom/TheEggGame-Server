package com.adedom.teg.controller

import com.adedom.teg.request.account.ImageProfile
import com.adedom.teg.request.account.PlayerInfo
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.PlayerResponse
import com.adedom.teg.service.TegService
import com.adedom.teg.transaction.DatabaseTransaction
import com.adedom.teg.util.jwt.player
import com.adedom.teg.util.validateAccessToken
import io.ktor.application.call
import io.ktor.locations.get
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

    get<PlayerInfo> {
        val response = PlayerResponse()
        val playerId = call.player?.playerId
        val message = when {
            playerId == null -> playerId.validateAccessToken()

            else -> {
                val player = DatabaseTransaction.getPlayer(playerId)
                response.success = true
                response.playerInfo = player
                "Fetch player success"
            }
        }
        response.message = message
        call.respond(response)
    }

}
