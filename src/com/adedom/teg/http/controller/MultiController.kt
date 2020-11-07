package com.adedom.teg.http.controller

import com.adedom.teg.business.multi.MultiService
import com.adedom.teg.models.request.MultiItemCollectionRequest
import com.adedom.teg.models.websocket.CreateRoomIncoming
import com.adedom.teg.models.websocket.RoomPeopleAllOutgoing
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
        val accessToken = call.request.header(TegConstant.ACCESS_TOKEN)

        playgroundRoom.add(this)

        playgroundRoom.send(service.fetchRooms().toJson())

        try {
            incoming
                .consumeAsFlow()
                .onEach { frame ->
                    val request = frame.fromJson<CreateRoomIncoming>()
                    val response = service.createRoom(accessToken, request)
                    if (response.success) {
                        playgroundRoom.send(service.fetchRooms().toJson())
                    }
                }
                .catch { }
                .collect()
        } finally {
            playgroundRoom.remove(this)
        }
    }

}
