package com.adedom.teg.controller

import com.adedom.teg.request.single.ItemCollection
import com.adedom.teg.response.BackpackResponse
import com.adedom.teg.service.TegService
import com.adedom.teg.transaction.DatabaseTransaction
import com.adedom.teg.util.jwt.player
import com.adedom.teg.util.validateAccessToken
import io.ktor.application.call
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

fun Route.singleController(service: TegService) {

    get<ItemCollection> {
        val response = BackpackResponse()
        val playerId = call.player?.playerId
        val message = when {
            playerId == null -> playerId.validateAccessToken()

            else -> {
                response.backpack = DatabaseTransaction.getBackpack(playerId)
                response.success = true
                "Fetch backpack success"
            }
        }
        response.message = message
        call.respond(response)
    }

}