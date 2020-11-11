package com.adedom.teg.http.controller

import com.adedom.teg.business.multi.MultiService
import com.adedom.teg.models.request.*
import com.adedom.teg.models.websocket.RoomPeopleAllOutgoing
import com.adedom.teg.util.TegConstant
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

    post<CreateRoomRequest> {
        val request = call.receive<CreateRoomRequest>()
        val response = service.createRoom(call.playerId, request)
        call.respond(response)
    }

    get<CurrentRoomNoRequest> {
        val response = service.currentRoomNo(call.playerId)
        call.respond(response)
    }

    post<JoinRoomInfoRequest> {
        val request = call.receive<JoinRoomInfoRequest>()
        val response = service.joinRoomInfo(call.playerId, request)
        call.respond(response)
    }

    delete<LeaveRoomInfoRequest> {
        val response = service.leaveRoomInfo(call.playerId)
        call.respond(response)
    }

    patch<ChangeTeamRequest> { request ->
        val response = service.changeTeam(call.playerId, request)
        call.respond(response)
    }

    patch<ChangeStatusRoomInfoRequest> {
        val response = service.changeStatusRoomInfo(call.playerId)
        call.respond(response)
    }

    patch<TegMultiRequest> {
        val response = service.tegMulti(call.playerId)
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
        playgroundRoom.add(this)

        playgroundRoom.send(service.fetchRooms().toJson())

        try {
            incoming
                .consumeAsFlow()
                .onEach { frame ->
                    playgroundRoom.send(service.fetchRooms().toJson())
                }
                .catch { }
                .collect()
        } finally {
            playgroundRoom.remove(this)
        }
    }

    val roomInfoTitle = mutableListOf<WebSocketSession>()
    webSocket("/websocket/multi/room-info-title") {
        val accessToken: String = call.request.header(TegConstant.ACCESS_TOKEN)!!

        roomInfoTitle.add(this)

        val roomNo: String = service.currentRoomNo(accessToken)
        roomInfoTitle.send(service.fetchRoomInfoTitle(roomNo).toJson())

        try {
            incoming
                .consumeAsFlow()
                .onEach { frame ->
                }
                .catch { }
                .collect()
        } finally {
            roomInfoTitle.remove(this)
        }
    }

    val roomInfoPlayers = mutableListOf<WebSocketSession>()
    webSocket("/websocket/multi/room-info-players") {
        val accessToken: String = call.request.header(TegConstant.ACCESS_TOKEN)!!

        roomInfoPlayers.add(this)

        val roomNo: String = service.currentRoomNo(accessToken)
        roomInfoPlayers.send(service.fetchRoomInfoPlayers(roomNo).toJson())

        try {
            incoming
                .consumeAsFlow()
                .onEach { frame ->
                    roomInfoPlayers.send(service.fetchRoomInfoPlayers(roomNo).toJson())
                }
                .catch { }
                .collect()
        } finally {
            roomInfoPlayers.remove(this)
        }
    }

}
