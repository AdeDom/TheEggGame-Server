package com.adedom.teg.data.database

import org.jetbrains.exposed.sql.Table

object Multis : Table(name = DatabaseConstant.multiTable) {

    val multiId = integer(name = DatabaseConstant.multiId).autoIncrement()
    val roomNo = varchar(name = DatabaseConstant.roomNo, length = 10)
    val latitude = double(name = DatabaseConstant.latitude)
    val longitude = double(name = DatabaseConstant.longitude)
    val status = varchar(name = DatabaseConstant.status, length = 10)

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(multiId, name = DatabaseConstant.multiPk)

}
