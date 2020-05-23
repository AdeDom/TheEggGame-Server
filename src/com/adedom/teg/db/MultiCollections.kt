package com.adedom.teg.db

import com.adedom.teg.models.MultiCollection
import com.adedom.teg.util.toDateFormat
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object MultiCollections : Table(name = DatabaseConstant.multiCollectionTable) {

    val collectionId = integer(name = DatabaseConstant.collectionId).autoIncrement()
    val roomNo = varchar(name = DatabaseConstant.roomNo, length = 10)
    val playerId = integer(name = DatabaseConstant.playerId)
    val score = integer(name = DatabaseConstant.score)
    val team = varchar(name = DatabaseConstant.team, length = 5)
    val latitude = double(name = DatabaseConstant.latitude)
    val longitude = double(name = DatabaseConstant.longitude)
    val dateTime = datetime(name = DatabaseConstant.dateTime)

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(collectionId, name = DatabaseConstant.multiCollectionPk)

    fun toMultiCollection(row: ResultRow) = MultiCollection(
        collectionId = row[collectionId],
        roomNo = row[roomNo],
        playerId = row[playerId],
        score = row[score],
        team = row[team],
        latitude = row[latitude],
        longitude = row[longitude],
        dateTime = row[dateTime].toDateFormat()
    )

}
