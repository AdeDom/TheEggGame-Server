package com.adedom.teg.refactor

import com.adedom.teg.data.database.ItemCollections
import com.adedom.teg.data.database.Players
import com.adedom.teg.data.database.RoomInfos
import com.adedom.teg.data.models.RoomInfoDb
import com.adedom.teg.util.toLevel
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.sum

object MapResponse {

    fun toRoomInfoDb(row: ResultRow) = RoomInfoDb(
        roomNo = row[RoomInfos.roomNo],
        latitude = row[RoomInfos.latitude],
        longitude = row[RoomInfos.longitude],
        team = row[RoomInfos.team],
        status = row[RoomInfos.status],
        playerId = row[Players.playerId],
        name = row[Players.name],
        image = row[Players.image],
        level = row[ItemCollections.qty.sum()].toLevel(),
        state = row[Players.state],
        gender = row[Players.gender]
    )

}
