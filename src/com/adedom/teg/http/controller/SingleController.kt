package com.adedom.teg.http.controller

import com.adedom.teg.models.request.BackpackRequest
import com.adedom.teg.models.request.ItemCollectionRequest
import com.adedom.teg.business.service.single.SingleService
import com.adedom.teg.util.playerId
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
