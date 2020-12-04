package com.adedom.teg.util

object DatabaseConfig {

    object Localhost {
        private const val databaseName: String = "the_egg_game"
        const val username: String = "root"
        const val password: String = "abc456"
        const val jdbcUrl: String = "jdbc:mysql://192.168.43.22:3306/$databaseName"
    }

    object Heroku {
        private const val databaseName: String = "heroku_1393de2d66fc96b"
        const val username: String = "bc162b7210edb9"
        const val password: String = "dae67b90"
        const val jdbcUrl: String =
            "jdbc:mysql://$username:$password@us-cdbr-east-05.cleardb.net/$databaseName?reconnect=true"
    }

}
