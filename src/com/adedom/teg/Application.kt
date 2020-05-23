package com.adedom.teg

import com.adedom.teg.controller.controller
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import io.ktor.routing.route
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.Database

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

    install(Routing) {
        route("/api") {
            controller()
        }
    }
}
