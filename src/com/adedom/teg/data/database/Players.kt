package com.adedom.teg.data.database

import org.jetbrains.exposed.sql.Table

object Players : Table(name = DatabaseConstant.playerTable) {
    val playerId = varchar(name = DatabaseConstant.playerId, length = 50)
    val username = varchar(name = DatabaseConstant.username, length = 50)
    val password = varchar(name = DatabaseConstant.password, length = 100)
    val name = varchar(name = DatabaseConstant.name, length = 50)
    val image = varchar(name = DatabaseConstant.image, length = 300).nullable()
    val gender = varchar(name = DatabaseConstant.gender, length = 5)
    val birthDate = long(name = DatabaseConstant.birthDate)
    val state = varchar(name = DatabaseConstant.state, length = 10).nullable()
    val latitude = double(name = DatabaseConstant.latitude).nullable()
    val longitude = double(name = DatabaseConstant.longitude).nullable()
    val dateTimeCreated = long(name = DatabaseConstant.dateTimeCreated)
    val dateTimeUpdated = long(name = DatabaseConstant.dateTimeUpdated).nullable()

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(playerId, name = DatabaseConstant.playerPk)

}
