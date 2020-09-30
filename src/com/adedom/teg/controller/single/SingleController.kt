package com.adedom.teg.controller.single

import com.adedom.teg.controller.single.model.BackpackRequest
import com.adedom.teg.controller.single.model.ItemCollectionRequest
import com.adedom.teg.service.single.SingleService
import com.adedom.teg.util.jwt.playerId
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

@KtorExperimentalLocationsAPI
fun Route.singleController(service: SingleService) {

    get<BackpackRequest> {
        val response = service.fetchItemCollection(call.playerId)
        call.respond(response)
    }

    post<ItemCollectionRequest> {
        val request = call.receive<ItemCollectionRequest>()
        val response = service.itemCollection(call.playerId, request)
        call.respond(response)
    }

}
