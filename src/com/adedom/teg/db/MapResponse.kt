package com.adedom.teg.db

import com.adedom.teg.models.LogActive
import com.adedom.teg.models.PlayerInfo
import com.adedom.teg.models.RoomInfo
import com.adedom.teg.util.toLevel
import org.jetbrains.exposed.sql.ResultRow

object MapResponse {

    fun toPlayers(row: ResultRow) = PlayerInfo(
        playerId = row[Players.playerId],
        username = row[Players.username],
        name = row[Players.name],
        image = row[Players.image],
        level = row[ItemCollections.level].toLevel(),
        state = row[Players.state],
        gender = row[Players.gender]
    )

    fun toRoomInfo(row: ResultRow) = RoomInfo(
        roomNo = row[RoomInfos.roomNo],
        latitude = row[RoomInfos.latitude],
        longitude = row[RoomInfos.longitude],
        team = row[RoomInfos.team],
        status = row[RoomInfos.status],
        playerId = row[Players.playerId],
        name = row[Players.name],
        image = row[Players.image],
        level = row[ItemCollections.level].toLevel(),
        state = row[Players.state],
        gender = row[Players.gender]
    )

    fun toLogActiveId(row: ResultRow) = LogActive(
        logId = row[LogActives.logId]
    )

}
