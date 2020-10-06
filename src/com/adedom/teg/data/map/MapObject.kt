package com.adedom.teg.data.map

import com.adedom.teg.data.database.ItemCollections
import com.adedom.teg.data.database.Players
import com.adedom.teg.models.models.PlayerInfoDb
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

}
