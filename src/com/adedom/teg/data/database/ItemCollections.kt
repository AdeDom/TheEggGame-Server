package com.adedom.teg.data.database

import com.adedom.teg.refactor.ItemCollection
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.sum

object ItemCollections : Table(name = DatabaseConstant.itemCollectionTable) {

    val collectionId = integer(name = DatabaseConstant.collectionId).autoIncrement()
    val playerId = varchar(name = DatabaseConstant.playerId, length = 50).references(Players.playerId)
    val itemId = integer(name = DatabaseConstant.itemId)
    val qty = integer(name = DatabaseConstant.qty)
    val latitude = double(name = DatabaseConstant.latitude)
    val longitude = double(name = DatabaseConstant.longitude)
    val dateTime = long(name = DatabaseConstant.dateTime)

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
        dateTime = row[dateTime]
    )

}
