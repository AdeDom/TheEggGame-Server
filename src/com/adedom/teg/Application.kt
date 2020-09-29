package com.adedom.teg

import com.adedom.teg.controller.account.accountController
import com.adedom.teg.controller.applicationController
import com.adedom.teg.controller.auth.authController
import com.adedom.teg.controller.headerController
import com.adedom.teg.controller.singleController
import com.adedom.teg.di.getBusinessModule
import com.adedom.teg.di.getDataModule
import com.adedom.teg.service.account.AccountService
import com.adedom.teg.service.auth.AuthService
import com.adedom.teg.service.teg.TegService
import com.adedom.teg.util.DatabaseConfig
import com.adedom.teg.util.DatabaseConfigMode
import com.adedom.teg.util.jwt.CommonJwt
import com.adedom.teg.util.jwt.JwtConfig
import com.adedom.teg.util.jwt.PlayerPrincipal
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.logging.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.locations.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.Database
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.koin.logger.SLF4JLogger

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalLocationsAPI
fun Application.module() {

    // database mysql
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

    // start project
    install(DefaultHeaders)
    install(CallLogging)

    // route location
    install(Locations)

    // gson convertor json
    install(ContentNegotiation) {
        gson {
        }
    }

    // jwt
    install(Authentication) {
        jwt {
            verifier(JwtConfig.verifier)
            realm = CommonJwt.REALM
            validate {
                val playerId = it.payload.getClaim(CommonJwt.PLAYER_ID).asString()
                if (playerId != null) {
                    PlayerPrincipal(playerId)
                } else {
                    null
                }
            }
        }
    }

    // koin dependencies injection
    install(Koin) {
        SLF4JLogger()
        modules(getDataModule, getBusinessModule)
    }
    val service: TegService by inject()
    val authService: AuthService by inject()
    val accountService: AccountService by inject()

    // route
    install(Routing) {
        authController(authService)

        authenticate {
            accountController(accountService)
            applicationController(service)
            singleController(service)
        }

        route("api") {
            authenticate {
                headerController()
            }
        }
    }
}

internal fun getHttpClientOkHttp() = HttpClient(OkHttp) {
    install(HttpTimeout) {
        requestTimeoutMillis = 60_000
    }

    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.HEADERS
    }
}
