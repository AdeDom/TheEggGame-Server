package com.adedom.teg.db

import com.adedom.teg.models.Multi
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object Multis : Table(name = DatabaseConstant.multiTable) {

    val multiId = integer(name = DatabaseConstant.multiId).autoIncrement()
    val roomNo = varchar(name = DatabaseConstant.roomNo, length = 10)
    val latitude = double(name = DatabaseConstant.latitude)
    val longitude = double(name = DatabaseConstant.longitude)
    val status = varchar(name = DatabaseConstant.status, length = 10)

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(multiId, name = DatabaseConstant.multiPk)

    fun toMulti(row: ResultRow) = Multi(
        multiId = row[multiId],
        roomNo = row[roomNo],
        latitude = row[latitude],
        longitude = row[longitude],
        status = row[status]
    )

}
