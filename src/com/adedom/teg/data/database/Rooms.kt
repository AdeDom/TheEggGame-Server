package com.adedom.teg.data.database

import org.jetbrains.exposed.sql.Table

object Rooms : Table(name = DatabaseConstant.roomTable) {

    val roomId = integer(name = DatabaseConstant.roomId).autoIncrement()
    val roomNo = varchar(name = DatabaseConstant.roomNo, length = 10)
    val name = varchar(name = DatabaseConstant.name, length = 100)
    val people = varchar(name = DatabaseConstant.people, length = 5)
    val status = varchar(name = DatabaseConstant.status, length = 10)
    val dateTime = long(name = DatabaseConstant.dateTime)

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(roomId, name = DatabaseConstant.roomPk)

}
