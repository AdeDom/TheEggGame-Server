package com.adedom.teg.controller.single

import com.adedom.teg.controller.single.model.ItemCollectionRequest
import com.adedom.teg.response.BackpackResponse
import com.adedom.teg.service.single.SingleService
import com.adedom.teg.util.jwt.player
import com.adedom.teg.util.jwt.playerId
import com.adedom.teg.util.validateAccessToken
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

@KtorExperimentalLocationsAPI
fun Route.singleController(service: SingleService) {

    get<ItemCollectionRequest> {
        val response = BackpackResponse()
        val playerId = call.player?.playerId
        val message = when {
            playerId == null -> playerId.validateAccessToken()

            else -> {
                val service: BackpackResponse = service.fetchItemCollection(playerId)
                response.success = service.success
                response.backpack = service.backpack
                service.message
            }
        }
        response.message = message
        call.respond(response)
    }

    post<ItemCollectionRequest> {
        val request = call.receive<ItemCollectionRequest>()
        val response = service.itemCollection(call.playerId, request)
        call.respond(response)
    }

}
