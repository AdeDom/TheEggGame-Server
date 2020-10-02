package com.adedom.teg.data.database

import com.adedom.teg.refactor.Room
import com.adedom.teg.util.toDateFormat
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object Rooms : Table(name = DatabaseConstant.roomTable) {

    val roomId = integer(name = DatabaseConstant.roomId).autoIncrement()
    val roomNo = varchar(name = DatabaseConstant.roomNo, length = 10)
    val name = varchar(name = DatabaseConstant.name, length = 100)
    val people = varchar(name = DatabaseConstant.people, length = 5)
    val status = varchar(name = DatabaseConstant.status, length = 10)
    val dateTime = datetime(name = DatabaseConstant.dateTime)

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(roomId, name = DatabaseConstant.roomPk)

    fun toRoom(row: ResultRow) = Room(
        roomId = row[roomId],
        roomNo = row[roomNo],
        name = row[name],
        people = row[people],
        status = row[status],
        dateTime = row[dateTime].toDateFormat()
    )

    fun toPeopleRoom(row: ResultRow) = Room(
        people = row[people]
    )

    fun toRoomNo(row: ResultRow) = Room(
        roomNo = row[roomNo]
    )

}
