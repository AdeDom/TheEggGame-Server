package com.adedom.teg.util

class DatabaseConfig(mode: DatabaseConfigMode) {

    var databaseName: String = ""
    var username: String = ""
    var password: String = ""
    var jdbcUrl: String = ""

    init {
        when (mode) {
            DatabaseConfigMode.DEVELOP -> {
                databaseName = "the_egg_game"
                username = "root"
                password = "abc456"
                jdbcUrl = "jdbc:mysql://192.168.43.22:3306/$databaseName"
            }
            DatabaseConfigMode.END_POINT -> {
                databaseName = "heroku_1393de2d66fc96b"
                username = "bc162b7210edb9"
                password = "dae67b90"
                jdbcUrl = "jdbc:mysql://$username:$password@us-cdbr-east-05.cleardb.net/$databaseName?reconnect=true"
            }
        }
    }

}

enum class DatabaseConfigMode {
    DEVELOP, END_POINT
}
