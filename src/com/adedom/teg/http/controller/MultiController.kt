package com.adedom.teg.http.controller

import com.adedom.teg.business.multi.MultiService
import com.adedom.teg.models.request.FetchRoomRequest
import com.adedom.teg.models.request.MultiItemCollectionRequest
import com.adedom.teg.util.playerId
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

@KtorExperimentalLocationsAPI
fun Route.multiController(service: MultiService) {

    post<MultiItemCollectionRequest> {
        val request = call.receive<MultiItemCollectionRequest>()
        val response = service.itemCollection(call.playerId, request)
        call.respond(response)
    }

    get<FetchRoomRequest> {
        val response = service.fetchRooms()
        call.respond(response)
    }

}
