package com.adedom.teg.data.database

import org.jetbrains.exposed.sql.Table

object RoomInfos : Table(name = DatabaseConstant.roomInfoTable) {

    val infoId = integer(name = DatabaseConstant.infoId).autoIncrement()
    val roomNo = varchar(name = DatabaseConstant.roomNo, length = 10)
    val playerId = varchar(name = DatabaseConstant.playerId, length = 50).references(Players.playerId)
    val latitude = double(name = DatabaseConstant.latitude)
    val longitude = double(name = DatabaseConstant.longitude)
    val team = varchar(name = DatabaseConstant.team, length = 5)
    val status = varchar(name = DatabaseConstant.status, length = 10)
    val dateTime = long(name = DatabaseConstant.dateTime)

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(infoId, name = DatabaseConstant.roomInfoPk)

}
