package com.adedom.teg.data.database

import org.jetbrains.exposed.sql.Table

object SingleItems : Table(name = DatabaseConstant.singleItem) {

    val singleId = integer(name = DatabaseConstant.singleId).autoIncrement()
    val itemId = integer(name = DatabaseConstant.itemId)
    val qty = integer(name = DatabaseConstant.qty)
    val latitude = double(name = DatabaseConstant.latitude)
    val longitude = double(name = DatabaseConstant.longitude)
    val playerId = varchar(name = DatabaseConstant.playerId, length = 50).references(Players.playerId).nullable()
    val status = varchar(name = DatabaseConstant.status, length = 10)
    val dateTimeCreated = long(name = DatabaseConstant.dateTimeCreated)
    val dateTimeUpdated = long(name = DatabaseConstant.dateTimeUpdated).nullable()

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(singleId, name = DatabaseConstant.singleItemPk)

}
