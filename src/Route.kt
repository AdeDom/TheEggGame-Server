package com.adedom.teg

import com.adedom.teg.models.PlayerResponse
import com.adedom.teg.models.Players
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
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
                    val player = transaction {
                        Players.select { Players.playerId eq playerId.toInt() }
                            .map {
                                Players.toPlayer(it)
                            }
                            .single()
                    }
                    response.success = true
                    response.message = "Fetch player"
                    response.player = player
                }
            }
            call.respond(response)
        }
    }

}
