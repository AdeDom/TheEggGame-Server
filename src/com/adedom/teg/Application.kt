package com.adedom.teg

import com.adedom.teg.business.account.AccountService
import com.adedom.teg.business.application.ApplicationService
import com.adedom.teg.business.auth.AuthService
import com.adedom.teg.business.di.getBusinessModule
import com.adedom.teg.business.jwtconfig.JwtConfig
import com.adedom.teg.business.jwtconfig.PlayerPrincipal
import com.adedom.teg.business.single.SingleService
import com.adedom.teg.data.di.getDataModule
import com.adedom.teg.http.controller.accountController
import com.adedom.teg.http.controller.applicationController
import com.adedom.teg.http.controller.authController
import com.adedom.teg.http.controller.singleController
import com.adedom.teg.refactor.headerController
import com.adedom.teg.util.DatabaseConfig
import com.adedom.teg.util.DatabaseConfigMode
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
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
        maximumPoolSize = 100
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

    // koin dependencies injection
    install(Koin) {
        SLF4JLogger()
        modules(getDataModule, getBusinessModule)
    }
    val authService: AuthService by inject()
    val accountService: AccountService by inject()
    val applicationService: ApplicationService by inject()
    val singleService: SingleService by inject()
    val jwtConfig: JwtConfig by inject()

    // jwt
    install(Authentication) {
        jwt {
            verifier(jwtConfig.verifier)
            realm = jwtConfig.realm
            validate {
                val playerId = it.payload.getClaim(jwtConfig.playerId).asString()
                if (playerId != null) {
                    PlayerPrincipal(playerId)
                } else {
                    null
                }
            }
        }
    }

    // route
    install(Routing) {
        authController(authService)

        authenticate {
            accountController(accountService)
            applicationController(applicationService)
            singleController(singleService)
        }

        route("api") {
            authenticate {
                headerController()
            }
        }
    }
}

internal fun getHttpClientOkHttp() = HttpClient(OkHttp) {
    install(JsonFeature) {
        serializer = GsonSerializer()
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 60_000
    }

    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.HEADERS
    }
}
