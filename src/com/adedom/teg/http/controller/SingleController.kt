package com.adedom.teg.http.controller

import com.adedom.teg.business.single.SingleService
import com.adedom.teg.models.request.BackpackRequest
import com.adedom.teg.models.request.ItemCollectionRequest
import com.adedom.teg.models.websocket.PeopleAllOutgoing
import com.adedom.teg.util.playerId
import com.adedom.teg.util.send
import com.adedom.teg.util.toJson
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach

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

@KtorExperimentalLocationsAPI
fun Route.singleWebSocket(service: SingleService) {

    val singlePeopleAllSocket = mutableListOf<WebSocketSession>()
    webSocket("/websocket/single/single-people-all") {
        singlePeopleAllSocket.add(this)
        singlePeopleAllSocket.send(PeopleAllOutgoing(singlePeopleAllSocket.size).toJson())
        try {
            incoming
                .consumeAsFlow()
                .onEach {
                }
                .catch { }
                .collect()
        } finally {
            singlePeopleAllSocket.remove(this)
            singlePeopleAllSocket.send(PeopleAllOutgoing(singlePeopleAllSocket.size).toJson())
        }
    }

}
