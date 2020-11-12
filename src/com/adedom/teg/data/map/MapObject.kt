package com.adedom.teg.data.map

import com.adedom.teg.data.database.*
import com.adedom.teg.data.models.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.sum

object MapObject {

    fun toPlayerInfoDb(row: ResultRow) = PlayerInfoDb(
        playerId = row[Players.playerId],
        username = row[Players.username],
        name = row[Players.name],
        image = row[Players.image],
        level = row[ItemCollections.qty.sum()],
        state = row[Players.state],
        gender = row[Players.gender],
        birthDate = row[Players.birthDate],
    )

    fun toPlayerIdDb(row: ResultRow) = PlayerIdDb(
        playerId = row[Players.playerId],
    )

    fun toLogActiveLogIdDb(row: ResultRow) = LogActiveLogIdDb(
        logId = row[LogActives.logId],
    )

    fun toItemCollectionDb(row: ResultRow) = ItemCollectionDb(
        collectionId = row[ItemCollections.collectionId],
        playerId = row[ItemCollections.playerId],
        itemId = row[ItemCollections.itemId],
        qty = row[ItemCollections.qty],
        latitude = row[ItemCollections.latitude],
        longitude = row[ItemCollections.longitude],
        dateTime = row[ItemCollections.dateTime],
    )

    fun toMultiDb(row: ResultRow) = MultiDb(
        multiId = row[Multis.multiId],
        roomNo = row[Multis.roomNo],
        latitude = row[Multis.latitude],
        longitude = row[Multis.longitude],
        status = row[Multis.status],
    )

    fun toRoomDb(row: ResultRow) = RoomDb(
        roomId = row[Rooms.roomId],
        roomNo = row[Rooms.roomNo],
        name = row[Rooms.name],
        people = row[Rooms.people],
        status = row[Rooms.status],
        startTime = row[Rooms.startTime],
        endTime = row[Rooms.endTime],
        dateTime = row[Rooms.dateTime],
    )

    fun toRoomInfoDb(row: ResultRow) = RoomInfoDb(
        infoId = row[RoomInfos.infoId],
        roomNo = row[RoomInfos.roomNo],
        playerId = row[RoomInfos.playerId],
        latitude = row[RoomInfos.latitude],
        longitude = row[RoomInfos.longitude],
        team = row[RoomInfos.team],
        status = row[RoomInfos.status],
        role = row[RoomInfos.role],
        dateTime = row[RoomInfos.dateTime],
    )

    fun toRoomInfoPlayersDb(row: ResultRow, role: String?, status: String?, team: String?) = RoomInfoPlayersDb(
        playerId = row[Players.playerId],
        username = row[Players.username],
        name = row[Players.name],
        image = row[Players.image],
        level = row[ItemCollections.qty.sum()],
        state = row[Players.state],
        gender = row[Players.gender],
        birthDate = row[Players.birthDate],
        roleRoomInfo = role,
        statusRoomInfo = status,
        teamRoomInfo = team,
    )

}
