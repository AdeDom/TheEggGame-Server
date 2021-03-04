package com.adedom.teg

import com.adedom.teg.business.account.AccountService
import com.adedom.teg.business.application.ApplicationService
import com.adedom.teg.business.auth.AuthService
import com.adedom.teg.business.di.getBusinessModule
import com.adedom.teg.business.jwtconfig.JwtConfig
import com.adedom.teg.business.jwtconfig.PlayerPrincipal
import com.adedom.teg.business.multi.MultiService
import com.adedom.teg.business.report.ReportService
import com.adedom.teg.business.single.SingleService
import com.adedom.teg.data.di.getDataModule
import com.adedom.teg.http.controller.*
import com.adedom.teg.util.DatabaseConfig
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
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.locations.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.Database
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.koin.logger.SLF4JLogger
import java.time.Duration

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalLocationsAPI
fun Application.module() {

    // database mysql
    val databaseConfig = DatabaseConfig.Heroku
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

    // Cross-Origin Resource Sharing
    install(CORS) {
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Put)
        method(HttpMethod.Patch)
        method(HttpMethod.Delete)
        method(HttpMethod.Head)
        method(HttpMethod.Options)
        host(host = "localhost:8080", schemes = listOf("http"))
        host(host = "localhost:8081", schemes = listOf("http"))
        host(host = "teg-report.herokuapp.com", schemes = listOf("https"))
        allowCredentials = true
        allowNonSimpleContentTypes = true
        maxAgeInSeconds = CORS.CORS_DEFAULT_MAX_AGE * 30
    }

    // web socket
    install(io.ktor.websocket.WebSockets) {
        pingPeriod = Duration.ofSeconds(60)
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
    val multiService: MultiService by inject()
    val reportService: ReportService by inject()
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
            multiController(multiService)
        }

        // web sockets
        singleWebSocket(singleService)
        multiWebSocket(multiService, jwtConfig)

        // report
        reportController(reportService)
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
