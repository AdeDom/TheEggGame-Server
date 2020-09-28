package com.adedom.teg.db

import com.adedom.teg.models.LogActive
import com.adedom.teg.util.toDateFormat
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object LogActives : Table(name = DatabaseConstant.logActiveTable) {

    val logId = integer(name = DatabaseConstant.logId).autoIncrement()
    val playerId = varchar(name = DatabaseConstant.playerId, length = 50)
    val dateTimeIn = datetime(name = DatabaseConstant.dateTimeIn)
    val dateTimeOut = datetime(name = DatabaseConstant.dateTimeOut).nullable()

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(logId, name = DatabaseConstant.logActivePk)

    fun toLogActive(row: ResultRow) = LogActive(
        logId = row[logId],
        playerId = row[playerId],
        dateTimeIn = row[dateTimeIn].toDateFormat(),
        dateTimeOut = row[dateTimeOut]?.toDateFormat()
    )

}
