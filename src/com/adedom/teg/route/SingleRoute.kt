package com.adedom.teg.route

import com.adedom.teg.request.ItemCollectionRequest
import com.adedom.teg.response.BackpackResponse
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.transaction.DatabaseTransaction
import com.adedom.teg.util.*
import com.adedom.teg.util.jwt.player
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.itemCollection() {

    route("item-collection") {
        get("/") {
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

        post("/") {
            val response = BaseResponse()
            val (playerId, itemId, qty, latitude, longitude) = call.receive<ItemCollectionRequest>()
            val message = when {
                playerId == null -> ItemCollectionRequest::playerId.name.validateEmpty()
                playerId <= 0 -> ItemCollectionRequest::playerId.name.validateLessEqZero()
                DatabaseTransaction.validatePlayer(playerId) -> ItemCollectionRequest::playerId.name.validateNotFound()

                itemId == null -> ItemCollectionRequest::itemId.name.validateEmpty()
                itemId <= 0 || itemId > CommonConstant.MAX_ITEM -> ItemCollectionRequest::itemId.name.validateIncorrect()

                qty == null -> ItemCollectionRequest::qty.name.validateEmpty()
                qty <= 0 -> ItemCollectionRequest::qty.name.validateIncorrect()

                latitude == null -> ItemCollectionRequest::latitude.name.validateEmpty()

                longitude == null -> ItemCollectionRequest::longitude.name.validateEmpty()

                else -> {
                    DatabaseTransaction.postItemCollection(
                        itemCollectionRequest = ItemCollectionRequest(
                            playerId = playerId,
                            itemId = itemId,
                            qty = qty,
                            latitude = latitude,
                            longitude = longitude
                        )
                    )
                    response.success = true
                    "Post item collection success"
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}
