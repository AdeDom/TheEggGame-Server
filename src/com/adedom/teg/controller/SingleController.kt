package com.adedom.teg.controller

import com.adedom.teg.request.single.ItemCollectionRequest
import com.adedom.teg.response.BackpackResponse
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.service.TegService
import com.adedom.teg.util.CommonConstant
import com.adedom.teg.util.jwt.player
import com.adedom.teg.util.validateAccessToken
import com.adedom.teg.util.validateEmpty
import com.adedom.teg.util.validateIncorrect
import io.ktor.application.call
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route

fun Route.singleController(service: TegService) {

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
        val response = BaseResponse()
        val (itemId, qty, latitude, longitude) = call.receive<ItemCollectionRequest>()
        val playerId = call.player?.playerId
        val message = when {
            playerId == null -> playerId.validateAccessToken()

            itemId == null -> it::itemId.name.validateEmpty()
            itemId <= 0 || itemId > CommonConstant.MAX_ITEM -> it::itemId.name.validateIncorrect()

            qty == null -> it::qty.name.validateEmpty()
            qty <= 0 -> it::qty.name.validateIncorrect()

            latitude == null -> it::latitude.name.validateEmpty()

            longitude == null -> it::longitude.name.validateEmpty()

            else -> {
                val service: BaseResponse = service.postItemCollection(
                    playerId,
                    ItemCollectionRequest(itemId, qty, latitude, longitude)
                )
                response.success = service.success
                service.message
            }
        }
        response.message = message
        call.respond(response)
    }

}
