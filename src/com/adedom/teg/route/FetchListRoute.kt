package com.adedom.teg.route

import com.adedom.teg.db.*
import com.adedom.teg.response.*
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.fetchList() {

    get("/item-collection") {
        val list = transaction {
            ItemCollections.selectAll()
                .map { ItemCollections.toItemCollection(it) }
        }
        val response = ItemCollectionsResponse(
            itemCollection = list
        )
        response.success = true
        response.message = "Fetch item collection success"
        call.respond(response)
    }

    get("/log-active") {
        val list = transaction {
            LogActives.selectAll()
                .map { LogActives.toLogActive(it) }
        }
        val response = LogActivesResponse(
            logActive = list
        )
        response.success = true
        response.message = "Fetch log active success"
        call.respond(response)
    }

    get("/multi") {
        val list = transaction {
            Multis.selectAll()
                .map { Multis.toMulti(it) }
        }
        val response = MultisResponse(
            multi = list
        )
        response.success = true
        response.message = "Fetch multi success"
        call.respond(response)
    }

    get("/multi-collection") {
        val list = transaction {
            MultiCollections.selectAll()
                .map { MultiCollections.toMultiCollection(it) }
        }
        val response = MultiCollectionsResponse(
            multiCollection = list
        )
        response.success = true
        response.message = "Fetch multi collection success"
        call.respond(response)
    }

    get("/room") {
        val list = transaction {
            Rooms.selectAll()
                .map { Rooms.toRoom(it) }
        }
        val response = RoomsResponse(
            room = list
        )
        response.success = true
        response.message = "Fetch room success"
        call.respond(response)
    }

    get("/room-info") {
        val list = transaction {
            RoomInfos.selectAll()
                .map { RoomInfos.toRoomInfo(it) }
        }
        val response = RoomInfosResponse(
            roomInfo = list
        )
        response.success = true
        response.message = "Fetch room info success"
        call.respond(response)
    }

}

