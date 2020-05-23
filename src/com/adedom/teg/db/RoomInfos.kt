package com.adedom.teg.db

import com.adedom.teg.models.RoomInfo
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object RoomInfos : Table(name = DatabaseConstant.roomInfoTable) {

    val infoId = integer(name = DatabaseConstant.infoId).autoIncrement()
    val roomNo = varchar(name = DatabaseConstant.roomNo, length = 10)
    val playerId = integer(name = DatabaseConstant.playerId)
    val latitude = double(name = DatabaseConstant.latitude)
    val longitude = double(name = DatabaseConstant.longitude)
    val team = varchar(name = DatabaseConstant.team, length = 5)
    val status = varchar(name = DatabaseConstant.status, length = 10)
    val dateTime = datetime(name = DatabaseConstant.dateTime)

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(infoId, name = DatabaseConstant.roomInfoPk)

    fun toRoomInfo(row: ResultRow) = RoomInfo(
        infoId = row[infoId],
        roomNo = row[roomNo],
        playerId = row[playerId],
        latitude = row[latitude],
        longitude = row[longitude],
        team = row[team],
        status = row[status],
        dateTime = row[dateTime]
    )

}
