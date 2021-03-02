package com.adedom.teg.data.map

import com.adedom.teg.data.database.*
import com.adedom.teg.data.models.*
import org.jetbrains.exposed.sql.ResultRow

internal class MapperImpl : Mapper {

    override fun itemCollection(row: ResultRow): ItemCollectionDb {
        return ItemCollectionDb(
            collectionId = row[ItemCollections.collectionId],
            playerId = row[ItemCollections.playerId],
            itemId = row[ItemCollections.itemId],
            qty = row[ItemCollections.qty],
            latitude = row[ItemCollections.latitude],
            longitude = row[ItemCollections.longitude],
            dateTime = row[ItemCollections.dateTime],
            mode = row[ItemCollections.mode],
        )
    }

    override fun logActive(row: ResultRow): LogActiveDb {
        return LogActiveDb(
            logId = row[LogActives.logId],
            playerId = row[LogActives.playerId],
            dateTimeIn = row[LogActives.dateTimeIn],
            dateTimeOut = row[LogActives.dateTimeOut],
        )
    }

    override fun multiCollection(row: ResultRow): MultiCollectionDb {
        return MultiCollectionDb(
            collectionId = row[MultiCollections.collectionId],
            roomNo = row[MultiCollections.roomNo],
            playerId = row[MultiCollections.playerId],
            team = row[MultiCollections.team],
            latitude = row[MultiCollections.latitude],
            longitude = row[MultiCollections.longitude],
            dateTime = row[MultiCollections.dateTime],
        )
    }

    override fun multiItem(row: ResultRow): MultiItemDb {
        return MultiItemDb(
            multiId = row[MultiItems.multiId],
            roomNo = row[MultiItems.roomNo],
            latitude = row[MultiItems.latitude],
            longitude = row[MultiItems.longitude],
            status = row[MultiItems.status],
            dateTimeCreated = row[MultiItems.dateTimeCreated],
            dateTimeUpdated = row[MultiItems.dateTimeUpdated],
        )
    }

    override fun player(row: ResultRow): PlayerDb {
        return PlayerDb(
            playerId = row[Players.playerId],
            username = row[Players.username],
            password = row[Players.password],
            name = row[Players.name],
            image = row[Players.image],
            gender = row[Players.gender],
            birthDate = row[Players.birthDate],
            state = row[Players.state],
            latitude = row[Players.latitude],
            longitude = row[Players.longitude],
            currentMode = row[Players.currentMode],
            dateTimeCreated = row[Players.dateTimeCreated],
            dateTimeUpdated = row[Players.dateTimeUpdated],
        )
    }

}
