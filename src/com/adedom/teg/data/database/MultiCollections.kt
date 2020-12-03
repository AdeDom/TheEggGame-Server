package com.adedom.teg.data.database

import org.jetbrains.exposed.sql.Table

object MultiCollections : Table(name = DatabaseConstant.multiCollectionTable) {

    val collectionId = integer(name = DatabaseConstant.collectionId).autoIncrement()
    val roomNo = varchar(name = DatabaseConstant.roomNo, length = 10)
    val playerId = varchar(name = DatabaseConstant.playerId, length = 50)
    val team = varchar(name = DatabaseConstant.team, length = 5)
    val latitude = double(name = DatabaseConstant.latitude)
    val longitude = double(name = DatabaseConstant.longitude)
    val dateTime = long(name = DatabaseConstant.dateTime)

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(collectionId, name = DatabaseConstant.multiCollectionPk)

}
