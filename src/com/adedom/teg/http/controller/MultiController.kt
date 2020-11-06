package com.adedom.teg.http.controller

import com.adedom.teg.business.multi.MultiService
import com.adedom.teg.models.request.MultiItemCollectionRequest
import com.adedom.teg.models.websocket.RoomPeopleAllOutgoing
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
fun Route.multiController(service: MultiService) {

    post<MultiItemCollectionRequest> {
        val request = call.receive<MultiItemCollectionRequest>()
        val response = service.itemCollection(call.playerId, request)
        call.respond(response)
    }

//    get<FetchRoomRequest> {
//        val response = service.fetchRooms()
//        call.respond(response)
//    }

}

@KtorExperimentalLocationsAPI
fun Route.multiWebSocket(service: MultiService) {

    val roomPeopleAllSocket = mutableListOf<WebSocketSession>()
    webSocket("/websocket/multi/room-people-all") {
        roomPeopleAllSocket.add(this)
        roomPeopleAllSocket.send(RoomPeopleAllOutgoing(roomPeopleAllSocket.size).toJson())
        try {
            incoming
                .consumeAsFlow()
                .onEach { frame ->
                }
                .catch { }
                .collect()
        } finally {
            roomPeopleAllSocket.remove(this)
            roomPeopleAllSocket.send(RoomPeopleAllOutgoing(roomPeopleAllSocket.size).toJson())
        }
    }

    val playgroundRoom = mutableListOf<WebSocketSession>()
    webSocket("/websocket/multi/playground-room") {
        playgroundRoom.add(this)

        val roomsResponse = service.fetchRooms()
        playgroundRoom.send(roomsResponse.toJson())

        try {
            incoming
                .consumeAsFlow()
                .onEach { frame ->
                    // TODO: 06/11/2563 create room
                }
                .catch { }
                .collect()
        } finally {
            playgroundRoom.remove(this)
        }
    }

}
