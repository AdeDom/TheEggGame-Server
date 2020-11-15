package com.adedom.teg.http.controller

import com.adedom.teg.business.jwtconfig.JwtConfig
import com.adedom.teg.business.single.SingleService
import com.adedom.teg.data.database.Players
import com.adedom.teg.data.database.SingleItems
import com.adedom.teg.data.map.MapObject
import com.adedom.teg.models.request.BackpackRequest
import com.adedom.teg.models.request.ItemCollectionRequest
import com.adedom.teg.models.websocket.PeopleAllOutgoing
import com.adedom.teg.models.websocket.SingleItemOutgoing
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
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

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
fun Route.singleWebSocket(service: SingleService, jwtConfig: JwtConfig) {

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

        val playerId = jwtConfig.decodeJwtGetPlayerId(accessToken)

        val statement = transaction {
            val (latitude, longitude) = Players.select { Players.playerId eq playerId }
                .map { Pair(it[Players.latitude], it[Players.longitude]) }
                .single()

            SingleItems.insert {
                it[SingleItems.itemId] = (1..3).random()
                it[SingleItems.qty] = 1
                it[SingleItems.latitude] = latitude!! + 0.05
                it[SingleItems.longitude] = longitude!! + 0.05
                it[SingleItems.status] = TegConstant.SINGLE_ITEM_STATUS_ON
                it[SingleItems.dateTimeCreated] = System.currentTimeMillis()
            }
        }

        if (statement.resultedValues?.size ?: 0 > 0) {
            val singleItems = transaction {
                SingleItems.select {
                    SingleItems.status eq TegConstant.SINGLE_ITEM_STATUS_ON
                }.map { MapObject.toSingleItemDb(it) }
            }

            singleItemSocket.send(SingleItemOutgoing(singleItems).toJson())
        }

        try {
            incoming
                .consumeAsFlow()
                .onEach {
                }
                .catch { }
                .collect()
        } finally {
            singleItemSocket.remove(this)
        }
    }

}
