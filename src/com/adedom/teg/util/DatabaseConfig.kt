package com.adedom.teg.util

class DatabaseConfig(mode: DatabaseMode) {

    var port: Int = 0
    var host: String = ""
    var databaseName: String = ""
    var username: String = ""
    var password: String = ""
    var jdbcUrl: String = ""

    init {
        when (mode) {
            DatabaseMode.DEBUG -> {
                port = 3306
                host = "192.168.43.22" //command ip config
                databaseName = "the_egg_game"
                username = "root"
                password = "abc456"
                jdbcUrl = "jdbc:mysql://$host:$port/$databaseName"
            }
            DatabaseMode.DEVELOP -> {
                port = 8080
                host = "0.0.0.0"
                databaseName = "heroku_1393de2d66fc96b"
                username = "bc162b7210edb9"
                password = "dae67b90"
                jdbcUrl = "jdbc:mysql://$username:$password@us-cdbr-east-05.cleardb.net/$databaseName?reconnect=true"
            }
        }
    }

}

enum class DatabaseMode {
    DEBUG, DEVELOP
}
