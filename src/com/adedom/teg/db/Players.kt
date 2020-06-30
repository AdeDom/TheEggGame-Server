package com.adedom.teg.db

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object Players : Table(name = DatabaseConstant.playerTable) {
    val playerId = Players.integer(name = DatabaseConstant.playerId).autoIncrement()
    val username = Players.varchar(name = DatabaseConstant.username, length = 50)
    val password = Players.varchar(name = DatabaseConstant.password, length = 100)
    val name = Players.varchar(name = DatabaseConstant.name, length = 50)
    val image = Players.varchar(name = DatabaseConstant.image, length = 50).nullable()
    val state = Players.varchar(name = DatabaseConstant.state, length = 10).nullable()
    val gender = Players.varchar(name = DatabaseConstant.gender, length = 5)
    val dateTime = datetime(name = DatabaseConstant.dateTime)

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(playerId, name = DatabaseConstant.playerPk)

}
