package com.adedom.teg.db

import com.adedom.teg.models.ItemCollection
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object ItemCollections : Table(name = "item_collection") {
    val collectionId = ItemCollections.integer(name = "collection_id").autoIncrement()
    val playerId = ItemCollections.integer(name = "player_id")
    val itemId = ItemCollections.integer(name = "item_id")
    val qty = ItemCollections.integer(name = "qty")
    val latitude = ItemCollections.double(name = "latitude")
    val longitude = ItemCollections.double(name = "longitude")
    val dateTime = datetime(name = "date_time")

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(collectionId, name = "PK_ItemCollection_ID")

    fun toItemCollection(row: ResultRow): ItemCollection {
        return ItemCollection(
            collectionId = row[collectionId],
            playerId = row[playerId],
            itemId = row[itemId],
            qty = row[qty],
            latitude = row[latitude],
            longitude = row[longitude],
            dateTime = row[dateTime]
        )
    }

}
