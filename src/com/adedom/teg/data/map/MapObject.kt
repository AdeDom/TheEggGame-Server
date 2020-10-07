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
        birthdate = row[Players.birthdate],
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
        dateTime = row[Rooms.dateTime],
    )

    fun toPeopleRoomDb(row: ResultRow) = RoomDb(
        people = row[Rooms.people],
    )

    fun toRoomNoDb(row: ResultRow) = RoomDb(
        roomNo = row[Rooms.roomNo],
    )

    // TODO: 07/10/2563 concern convert level
    fun toRoomInfoDb(row: ResultRow) = RoomInfoDb(
        roomNo = row[RoomInfos.roomNo],
        latitude = row[RoomInfos.latitude],
        longitude = row[RoomInfos.longitude],
        team = row[RoomInfos.team],
        status = row[RoomInfos.status],
        playerId = row[Players.playerId],
        name = row[Players.name],
        image = row[Players.image],
        level = row[ItemCollections.qty.sum()],
        state = row[Players.state],
        gender = row[Players.gender],
    )

}
