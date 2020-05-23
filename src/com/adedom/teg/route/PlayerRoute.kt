package com.adedom.teg.route

import com.adedom.teg.db.ItemCollections
import com.adedom.teg.db.Players
import com.adedom.teg.response.PlayerResponse
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.user() {

    route("/get-player") {
        get("/{player_id}") {
            val response = PlayerResponse()
            val playerId = call.parameters["player_id"]
            when {
                playerId.isNullOrBlank() -> response.message = "Please enter player id"
                playerId.toInt() <= 0 -> response.message = "Please check the player id again"
                else -> {
                    val count = transaction {
                        Players.select { Players.playerId eq playerId.toInt() }
                            .count()
                            .toInt()
                    }
                    if (count == 0) {
                        response.message = "Not found"
                    } else {
                        val level = transaction {
                            ItemCollections.select { ItemCollections.playerId eq playerId.toInt() }
                                .andWhere { ItemCollections.itemId eq 1 }
                                .map { ItemCollections.toItemCollection(it) }
                                .sumBy { it.qty!! }
                                .div(1000)
                        }
                        val player = transaction {
                            Players.select { Players.playerId eq playerId.toInt() }
                                .map { Players.toPlayer(it, level) }
                                .single()
                        }
                        response.success = true
                        response.message = "Fetch player"
                        response.player = player
                    }
                }
            }
            call.respond(response)
        }
    }

}
