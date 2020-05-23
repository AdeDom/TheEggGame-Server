package com.adedom.teg.db

import com.adedom.teg.models.LogActive
import com.adedom.teg.util.toDateFormat
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object LogActives : Table(name = DatabaseConstant.logActiveTable) {

    val logId = integer(name = DatabaseConstant.logId).autoIncrement()
    val logKey = varchar(name = DatabaseConstant.logKey, length = 50)
    val playerId = integer(name = DatabaseConstant.playerId)
    val dateTimeIn = datetime(name = DatabaseConstant.dateTimeIn)
    val dateTimeOut = datetime(name = DatabaseConstant.dateTimeOut)

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(logId, name = DatabaseConstant.logActivePk)

    fun toLogActive(row: ResultRow) = LogActive(
        logId = row[logId],
        logKey = row[logKey],
        playerId = row[playerId],
        dateTimeIn = row[dateTimeIn].toDateFormat(),
        dateTimeOut = row[dateTimeOut].toDateFormat()
    )

}
