package com.adedom.teg.models

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

data class ItemCollection(
    val collectionId: Int? = null,
    val playerId: Int? = null,
    val itemId: Int? = null,
    val qty: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val dateTime: DateTime? = null
)

object ItemCollections : Table(name = "item_collection") {
    val collectionId = integer(name = "collection_id").autoIncrement()
    val playerId = integer(name = "player_id")
    val itemId = integer(name = "item_id")
    val qty = integer(name = "qty")
    val latitude = double(name = "latitude")
    val longitude = double(name = "longitude")
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
