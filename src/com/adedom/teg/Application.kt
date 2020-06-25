package com.adedom.teg

import com.adedom.teg.controller.connectionController
import com.adedom.teg.controller.headerController
import com.adedom.teg.db.Players
import com.adedom.teg.models.Player
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.util.jwt.CommonJwt
import com.adedom.teg.util.jwt.JwtConfig
import com.adedom.teg.util.jwt.PlayerPrincipal
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.jwt
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    Database.connect(
        "jdbc:mysql://us-cdbr-east-05.cleardb.net:3306/heroku_1393de2d66fc96b?reconnect=true",
        driver = "com.mysql.jdbc.Driver",
        user = "bc162b7210edb9",
        password = "dae67b90"
    )

    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        jackson {
        }
    }

    install(Authentication) {
        jwt {
            verifier(JwtConfig.verifier)
            realm = CommonJwt.REALM
            validate {
                val playerId = it.payload.getClaim(CommonJwt.PLAYER_ID).asInt()
                val username = it.payload.getClaim(CommonJwt.USERNAME).asString()
                PlayerPrincipal(playerId, username)
            }
        }
    }

    install(Routing) {
        route("api") {
            connectionController()

            authenticate {
                headerController()
            }
        }

        get("test") {
            call.respond("Hello AdeDom..")
        }

        get("/api/controller/action") {
            val response = BaseResponse()
            call.respond(response)
        }

        get("sample") {
            val players = transaction {
                Players.selectAll()
                    .map { row ->
                        Player(
                            playerId = row[Players.playerId],
                            username = row[Players.username],
                            name = row[Players.name],
                            image = row[Players.image],
                            state = row[Players.state],
                            gender = row[Players.gender]
                        )
                    }
            }
            call.respond(players)
        }
    }
}
