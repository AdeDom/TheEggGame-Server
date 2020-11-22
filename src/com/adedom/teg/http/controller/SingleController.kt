package com.adedom.teg.http.controller

import com.adedom.teg.business.single.SingleService
import com.adedom.teg.models.TegLatLng
import com.adedom.teg.models.request.BackpackRequest
import com.adedom.teg.models.request.SingleItemRequest
import com.adedom.teg.models.response.PlayerInfo
import com.adedom.teg.models.websocket.PeopleAllOutgoing
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
fun Route.singleController(service: SingleService) {

    get<BackpackRequest> {
        val response = service.fetchItemCollection(call.playerId)
        call.respond(response)
    }

    patch<SingleItemRequest> {
        val response = service.itemCollection(call.playerId, it)
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

    val singleItemSocket = mutableListOf<WebSocketSession>()
    webSocket("/websocket/single/single-item") {
        val accessToken: String = call.request.header(TegConstant.ACCESS_TOKEN)!!

        singleItemSocket.add(this)

        singleItemSocket.send(service.singleItem(accessToken).toJson())

        try {
            incoming
                .consumeAsFlow()
                .onEach {
                    singleItemSocket.send(service.singleItem(accessToken).toJson())
                }
                .catch { }
                .collect()
        } finally {
            singleItemSocket.remove(this)
        }
    }

    val singleSuccessAnnouncementSocket = mutableListOf<WebSocketSession>()
    webSocket("/websocket/single/single-success-announcement") {
        val accessToken: String = call.request.header(TegConstant.ACCESS_TOKEN)!!

        singleSuccessAnnouncementSocket.add(this)

        try {
            incoming
                .consumeAsFlow()
                .onEach {
                    singleSuccessAnnouncementSocket.send(service.singleSuccessAnnouncement(accessToken).toJson())
                }
                .catch { }
                .collect()
        } finally {
            singleSuccessAnnouncementSocket.remove(this)
        }
    }

    val playgroundSinglePlayerSocket = mutableListOf<WebSocketSession>()
    webSocket("/websocket/single/playground-single-player") {
        val accessToken: String = call.request.header(TegConstant.ACCESS_TOKEN)!!

        playgroundSinglePlayerSocket.add(this)

        val playgroundSinglePlayerOutgoing = service.fetchPlaygroundSinglePlayer()
        playgroundSinglePlayerSocket.send(playgroundSinglePlayerOutgoing.toJson())

        try {
            incoming
                .consumeAsFlow()
                .onEach { frame ->
                    val latLng = frame.fromJson<TegLatLng>()

                    playgroundSinglePlayerSocket.send(
                        service.setPlaygroundSinglePlayer(
                            playgroundSinglePlayerOutgoing.players as MutableList<PlayerInfo>,
                            accessToken,
                            latLng
                        ).toJson()
                    )
                }
                .catch { }
                .collect()
        } finally {
            playgroundSinglePlayerSocket.remove(this)
        }
    }

}
