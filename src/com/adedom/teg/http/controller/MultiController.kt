package com.adedom.teg.http.controller

import com.adedom.teg.business.multi.MultiService
import com.adedom.teg.models.request.FetchRoomRequest
import com.adedom.teg.models.request.MultiItemCollectionRequest
import com.adedom.teg.models.websocket.RoomListSocket
import com.adedom.teg.util.*
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

fun Route.multiWebSocket(){

    // socket + token
    val roomListSocket = mutableListOf<WebSocketSession>()
    webSocket("/websocket/multi/room-list") {
        roomListSocket.add(this)
        try {
            incoming
                .consumeAsFlow()
                .onEach { frame ->
                    val response = RoomListSocket(
                        peopleAll = roomListSocket.size,
                    ).toJson()
                    roomListSocket.send(response)
                }
                .catch { }
                .collect()
        } finally {
            roomListSocket.remove(this)
        }
    }

}
