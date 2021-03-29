package com.adedom.teg.util

/**
 * pathiphondev@gmail.com
 */
object DatabaseConfig {

    // hostName -> open terminal [ipconfig] using => IPv4 Address
    object Localhost {
        private const val hostName: String = ""
        private const val databaseName: String = ""
        const val username: String = ""
        const val password: String = ""
        const val jdbcUrl: String = "jdbc:mysql://$hostName:3306/$databaseName"
    }

    object Heroku {
        private const val hostName: String = ""
        private const val databaseName: String = ""
        const val username: String = ""
        const val password: String = ""
        const val jdbcUrl: String = "jdbc:mysql://$username:$password@$hostName/$databaseName?reconnect=true"
    }

}
