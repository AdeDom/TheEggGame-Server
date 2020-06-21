package com.adedom.teg

import com.adedom.teg.controller.connectionController
import com.adedom.teg.controller.headerController
import com.adedom.teg.db.Players
import com.adedom.teg.models.Player
import com.adedom.teg.util.jwt.CommonJwt
import com.adedom.teg.util.jwt.JwtConfig
import com.adedom.teg.util.jwt.PlayerPrincipal
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
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
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    val port = 3306
    val host = "192.168.43.22"
    val config = HikariConfig().apply {
        jdbcUrl = "jdbc:mysql://$host:$port/the_egg_game"
        driverClassName = "com.mysql.cj.jdbc.Driver"
        username = "root"
        password = "abc456"
        maximumPoolSize = 10
    }
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)

    embeddedServer(
        factory = Netty,
        port = port,
        host = host,
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
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

        route("test") {
            get("/") {
                call.respond("Hello AdeDom..")
            }

            get("/sample") {
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
}
