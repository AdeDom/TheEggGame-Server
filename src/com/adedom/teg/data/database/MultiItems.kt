package com.adedom.teg.data.database

import org.jetbrains.exposed.sql.Table

object MultiItems : Table(name = DatabaseConstant.multiItemTable) {

    val multiId = integer(name = DatabaseConstant.multiId).autoIncrement()
    val roomNo = varchar(name = DatabaseConstant.roomNo, length = 10)
    val playerId = varchar(name = DatabaseConstant.playerId, length = 50).references(Players.playerId)
    val latitude = double(name = DatabaseConstant.latitude)
    val longitude = double(name = DatabaseConstant.longitude)
    val status = varchar(name = DatabaseConstant.status, length = 10)
    val dateTimeCreated = long(name = DatabaseConstant.dateTimeCreated)
    val dateTimeUpdated = long(name = DatabaseConstant.dateTimeUpdated).nullable()

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(multiId, name = DatabaseConstant.multiPk)

}
