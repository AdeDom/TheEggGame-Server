package com.adedom.teg.db

import com.adedom.teg.models.ItemCollection
import com.adedom.teg.util.toDateFormat
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime
import org.jetbrains.exposed.sql.sum

object ItemCollections : Table(name = DatabaseConstant.itemCollectionTable) {

    val collectionId = ItemCollections.integer(name = DatabaseConstant.collectionId).autoIncrement()
    val playerId = ItemCollections.integer(name = DatabaseConstant.playerId).references(Players.playerId)
    val itemId = ItemCollections.integer(name = DatabaseConstant.itemId)
    val qty = ItemCollections.integer(name = DatabaseConstant.qty)
    val latitude = ItemCollections.double(name = DatabaseConstant.latitude)
    val longitude = ItemCollections.double(name = DatabaseConstant.longitude)
    val dateTime = datetime(name = DatabaseConstant.dateTime)

    val level = qty.sum()

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(collectionId, name = DatabaseConstant.itemCollectionPk)

    fun toItemCollection(row: ResultRow) = ItemCollection(
        collectionId = row[collectionId],
        playerId = row[playerId],
        itemId = row[itemId],
        qty = row[qty],
        latitude = row[latitude],
        longitude = row[longitude],
        dateTime = row[dateTime].toDateFormat()
    )

}
