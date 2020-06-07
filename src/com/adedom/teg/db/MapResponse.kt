package com.adedom.teg.db

import com.adedom.teg.models.RoomInfo
import org.jetbrains.exposed.sql.ResultRow

object MapResponse {

    fun toRoomInfo(row: ResultRow) = RoomInfo(
        roomNo = row[RoomInfos.roomNo],
        latitude = row[RoomInfos.latitude],
        longitude = row[RoomInfos.longitude],
        team = row[RoomInfos.team],
        status = row[RoomInfos.status],
        playerId = row[Players.playerId],
        name = row[Players.name],
        image = row[Players.image],
        level = row[ItemCollections.level]?.div(1000),
        state = row[Players.state],
        gender = row[Players.gender]
    )

}
