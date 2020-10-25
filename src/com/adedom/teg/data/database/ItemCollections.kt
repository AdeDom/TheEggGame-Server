package com.adedom.teg.data.database

import org.jetbrains.exposed.sql.Table

object ItemCollections : Table(name = DatabaseConstant.itemCollectionTable) {

    val collectionId = integer(name = DatabaseConstant.collectionId).autoIncrement()
    val playerId = varchar(name = DatabaseConstant.playerId, length = 50).references(Players.playerId)
    val itemId = integer(name = DatabaseConstant.itemId)
    val qty = integer(name = DatabaseConstant.qty)
    val latitude = double(name = DatabaseConstant.latitude)
    val longitude = double(name = DatabaseConstant.longitude)
    val dateTime = long(name = DatabaseConstant.dateTime)
    val mode = varchar(name = DatabaseConstant.mode, length = 20)

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(collectionId, name = DatabaseConstant.itemCollectionPk)

}
