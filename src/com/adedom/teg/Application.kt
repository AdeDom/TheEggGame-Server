package com.adedom.teg

import com.adedom.teg.controller.authController
import com.adedom.teg.controller.headerController
import com.adedom.teg.controller.imageController
import com.adedom.teg.di.tegAppModule
import com.adedom.teg.service.TegService
import com.adedom.teg.util.DatabaseConfig
import com.adedom.teg.util.DatabaseConfigMode
import com.adedom.teg.util.jwt.CommonJwt
import com.adedom.teg.util.jwt.JwtConfig
import com.adedom.teg.util.jwt.PlayerPrincipal
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.jwt
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.locations.Locations
import io.ktor.routing.Routing
import io.ktor.routing.route
import org.jetbrains.exposed.sql.Database
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.koin.logger.SLF4JLogger

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {

//    val databaseConfig = DatabaseConfig(DatabaseConfigMode.DEVELOP)
    val databaseConfig = DatabaseConfig(DatabaseConfigMode.END_POINT)

    val config = HikariConfig().apply {
        jdbcUrl = databaseConfig.jdbcUrl
        driverClassName = "com.mysql.cj.jdbc.Driver"
        username = databaseConfig.username
        password = databaseConfig.password
        maximumPoolSize = 10
    }
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)

    install(DefaultHeaders)
    install(CallLogging)
    install(Locations)
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
                if (playerId != null) {
                    PlayerPrincipal(playerId)
                } else {
                    null
                }
            }
        }
    }

    install(Koin) {
        SLF4JLogger()
        modules(tegAppModule)
    }
    val service: TegService by inject()

    install(Routing) {
        imageController()
        authController(service)

        route("api") {
            authenticate {
                headerController(service)
            }
        }
    }
}
