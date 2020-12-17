package com.adedom.teg.http.controller

import com.adedom.teg.business.jwtconfig.JwtConfig
import com.adedom.teg.business.multi.MultiService
import com.adedom.teg.models.request.*
import com.adedom.teg.models.websocket.PeopleAllOutgoing
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

    patch<RoomInfoTegMultiRequest> {
        val response = service.roomInfoTegMulti(call.playerId)
        call.respond(response)
    }

    patch<ChangeStatusUnreadyRequest> {
        val response = service.changeStatusUnready(call.playerId)
        call.respond(response)
    }

    get<FetchMultiPlayerRequest> {
        val response = service.fetchMultiPlayer(call.playerId)
        call.respond(response)
    }

    get<FetchMultiScoreRequest> {
        val response = service.fetchMultiScore(call.playerId)
        call.respond(response)
    }

    post<AddMultiScoreRequest> {
        val request = call.receive<AddMultiScoreRequest>()
        val response = service.addMultiScore(call.playerId, request)
        call.respond(response)
    }

    get<FetchMultiItemRequest> {
        val response = service.fetchMultiItem(call.playerId)
        call.respond(response)
    }

    post<AddMultiItemRequest> {
        val response = service.addMultiItem(call.playerId)
        call.respond(response)
    }

}

@KtorExperimentalLocationsAPI
fun Route.multiWebSocket(service: MultiService, jwtConfig: JwtConfig) {

    val roomPeopleAllSocket = mutableListOf<WebSocketSession>()
    webSocket("/websocket/multi/room-people-all") {
        roomPeopleAllSocket.add(this)
        roomPeopleAllSocket.send(PeopleAllOutgoing(roomPeopleAllSocket.size).toJson())
        try {
            incoming
                .consumeAsFlow()
                .onEach {
                }
                .catch { }
                .collect()
        } finally {
            roomPeopleAllSocket.remove(this)
            roomPeopleAllSocket.send(PeopleAllOutgoing(roomPeopleAllSocket.size).toJson())
        }
    }

    val playgroundRoom = mutableListOf<WebSocketSession>()
    webSocket("/websocket/multi/playground-room") {
        playgroundRoom.add(this)

        playgroundRoom.send(service.fetchRooms().toJson())

        try {
            incoming
                .consumeAsFlow()
                .onEach {
                    playgroundRoom.send(service.fetchRooms().toJson())
                }
                .catch { }
                .collect()
        } finally {
            playgroundRoom.remove(this)
        }
    }

    val roomInfoTitle = mutableListOf<Pair<WebSocketSession, String>>()
    webSocket("/websocket/multi/room-info-title") {
        val accessToken: String = call.request.header(TegConstant.ACCESS_TOKEN)!!

        val roomNo: String = service.currentRoomNo(accessToken)
        roomInfoTitle.add(Pair(this, roomNo))

        roomInfoTitle.filter { it.second == roomNo }
            .onEach { it.first.send(service.fetchRoomInfoTitle(roomNo).toJson()) }

        try {
            incoming
                .consumeAsFlow()
                .onEach {
                }
                .catch { }
                .collect()
        } finally {
            roomInfoTitle.remove(Pair(this, roomNo))
        }
    }

    val roomInfoPlayers = mutableListOf<Pair<WebSocketSession, String>>()
    webSocket("/websocket/multi/room-info-players") {
        val accessToken: String = call.request.header(TegConstant.ACCESS_TOKEN)!!

        val roomNo: String = service.currentRoomNo(accessToken)
        roomInfoPlayers.add(Pair(this, roomNo))

        roomInfoPlayers.filter { it.second == roomNo }
            .onEach { it.first.send(service.fetchRoomInfoPlayers(roomNo).toJson()) }

        try {
            incoming
                .consumeAsFlow()
                .onEach {
                    roomInfoPlayers.filter { it.second == roomNo }
                        .onEach { it.first.send(service.fetchRoomInfoPlayers(roomNo).toJson()) }
                }
                .catch { }
                .collect()
        } finally {
            roomInfoPlayers.remove(Pair(this, roomNo))
        }
    }

    val roomInfoTegMulti = mutableListOf<Pair<WebSocketSession, String>>()
    webSocket("/websocket/multi/room-info-teg-multi") {
        val accessToken: String = call.request.header(TegConstant.ACCESS_TOKEN)!!

        val roomNo: String = service.currentRoomNo(accessToken)
        roomInfoTegMulti.add(Pair(this, roomNo))

        try {
            incoming
                .consumeAsFlow()
                .onEach {
                    roomInfoTegMulti.filter { it.second == roomNo }
                        .onEach { it.first.send("") }
                }
                .catch { }
                .collect()
        } finally {
            roomInfoTegMulti.remove(Pair(this, roomNo))
        }
    }

    val multiPlayerItems = mutableListOf<Pair<WebSocketSession, String>>()
    webSocket("/websocket/multi/multi-player-items") {
        val accessToken: String = call.request.header(TegConstant.ACCESS_TOKEN)!!
        val playerId = jwtConfig.decodeJwtGetPlayerId(accessToken)

        val roomNo: String = service.currentRoomNo(accessToken)
        multiPlayerItems.add(Pair(this, roomNo))

        try {
            incoming
                .consumeAsFlow()
                .onEach {
                    multiPlayerItems.filter { it.second == roomNo }
                        .onEach { it.first.send(service.fetchMultiItem(playerId).toJson()) }
                }
                .catch { }
                .collect()
        } finally {
            multiPlayerItems.remove(Pair(this, roomNo))
        }
    }

    val multiPlayerScore = mutableListOf<Pair<WebSocketSession, String>>()
    webSocket("/websocket/multi/multi-player-score") {
        val accessToken: String = call.request.header(TegConstant.ACCESS_TOKEN)!!
        val playerId = jwtConfig.decodeJwtGetPlayerId(accessToken)

        val roomNo: String = service.currentRoomNo(accessToken)
        multiPlayerScore.add(Pair(this, roomNo))

        try {
            incoming
                .consumeAsFlow()
                .onEach {
                    multiPlayerScore.filter { it.second == roomNo }
                        .onEach { it.first.send(service.fetchMultiScore(playerId).toJson()) }
                }
                .catch { }
                .collect()
        } finally {
            multiPlayerScore.remove(Pair(this, roomNo))
        }
    }

    val multiPlayerEndTeg = mutableListOf<Pair<WebSocketSession, String>>()
    webSocket("/websocket/multi/multi-player-end-teg") {
        val accessToken: String = call.request.header(TegConstant.ACCESS_TOKEN)!!

        val roomNo: String = service.currentRoomNo(accessToken)
        multiPlayerEndTeg.add(Pair(this, roomNo))

        try {
            incoming
                .consumeAsFlow()
                .onEach {
                    multiPlayerEndTeg.filter { it.second == roomNo }
                        .onEach { it.first.send("") }
                }
                .catch { }
                .collect()
        } finally {
            multiPlayerEndTeg.remove(Pair(this, roomNo))
        }
    }

}
