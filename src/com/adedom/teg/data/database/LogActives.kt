package com.adedom.teg.data.database

import org.jetbrains.exposed.sql.Table

object LogActives : Table(name = DatabaseConstant.logActiveTable) {

    val logId = integer(name = DatabaseConstant.logId).autoIncrement()
    val playerId = varchar(name = DatabaseConstant.playerId, length = 50).references(Players.playerId)
    val dateTimeIn = long(name = DatabaseConstant.dateTimeIn)
    val dateTimeOut = long(name = DatabaseConstant.dateTimeOut).nullable()

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(logId, name = DatabaseConstant.logActivePk)

}
