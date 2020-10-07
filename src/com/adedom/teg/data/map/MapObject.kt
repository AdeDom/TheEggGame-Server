package com.adedom.teg.data.map

import com.adedom.teg.data.database.ItemCollections
import com.adedom.teg.data.database.LogActives
import com.adedom.teg.data.database.Players
import com.adedom.teg.data.models.LogActiveLogIdDb
import com.adedom.teg.data.models.PlayerIdDb
import com.adedom.teg.data.models.PlayerInfoDb
import org.jetbrains.exposed.sql.ResultRow

object MapObject {

    fun toPlayerInfoDb(row: ResultRow) = PlayerInfoDb(
        playerId = row[Players.playerId],
        username = row[Players.username],
        name = row[Players.name],
        image = row[Players.image],
        level = row[ItemCollections.level],
        state = row[Players.state],
        gender = row[Players.gender],
        birthdate = row[Players.birthdate],
    )

    fun toPlayerIdDb(row: ResultRow) = PlayerIdDb(
        playerId = row[Players.playerId],
    )

    fun toLogActiveLogId(row: ResultRow) = LogActiveLogIdDb(
        logId = row[LogActives.logId],
    )

}
