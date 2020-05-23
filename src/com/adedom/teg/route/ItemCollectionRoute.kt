package com.adedom.teg.route

import com.adedom.teg.db.ItemCollections
import com.adedom.teg.response.ItemCollectionsResponse
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

}

