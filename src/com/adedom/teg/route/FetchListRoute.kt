package com.adedom.teg.route

import com.adedom.teg.response.*
import com.adedom.teg.transaction.DatabaseTransaction
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get

fun Route.fetchList() {

    get("/item-collection") {
        val list = DatabaseTransaction.fetchItemCollection()
        val response = ItemCollectionsResponse(
            itemCollection = list
        )
        response.success = true
        response.message = "Fetch item collection success"
        call.respond(response)
    }

    get("/log-active") {
        val list = DatabaseTransaction.fetchLogActive()
        val response = LogActivesResponse(
            logActive = list
        )
        response.success = true
        response.message = "Fetch log active success"
        call.respond(response)
    }

    get("/multi") {
        val list = DatabaseTransaction.fetchMulti()
        val response = MultisResponse(
            multi = list
        )
        response.success = true
        response.message = "Fetch multi success"
        call.respond(response)
    }

    get("/multi-collection") {
        val list = DatabaseTransaction.fetchMultiCollection()
        val response = MultiCollectionsResponse(
            multiCollection = list
        )
        response.success = true
        response.message = "Fetch multi collection success"
        call.respond(response)
    }

    get("/room") {
        val list = DatabaseTransaction.fetchRoom()
        val response = RoomsResponse(
            room = list
        )
        response.success = true
        response.message = "Fetch room success"
        call.respond(response)
    }

}
