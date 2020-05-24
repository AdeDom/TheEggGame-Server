package com.adedom.teg.route

import com.adedom.teg.db.ItemCollections
import com.adedom.teg.db.Players
import com.adedom.teg.response.PlayerResponse
import com.adedom.teg.util.validateEmpty
import com.adedom.teg.util.validateLessEqZero
import com.adedom.teg.util.validateNotFound
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.user() {

    route("get-player") {
        val playerIdKey = "player_id"
        get("/{$playerIdKey}") {
            val response = PlayerResponse()
            val playerId = call.parameters[playerIdKey]
            val message = when {
                playerId.isNullOrBlank() -> playerIdKey.validateEmpty()
                playerId.toInt() <= 0 -> playerIdKey.validateLessEqZero()
                else -> {
                    val count = transaction {
                        Players.select { Players.playerId eq playerId.toInt() }
                            .count()
                            .toInt()
                    }
                    if (count == 0) {
                        playerIdKey.validateNotFound()
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
                        response.player = player
                        "Fetch player"
                    }
                }
            }
            response.message = message
            call.respond(response)
        }
    }

}
