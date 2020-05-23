package com.adedom.teg.db

import com.adedom.teg.models.Player
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object Players : Table(name = "player") {
    val playerId = Players.integer(name = "player_id").autoIncrement()
    val username = Players.varchar("username", length = 50)
    val name = Players.varchar("name", length = 50)
    val image = Players.varchar("image", length = 50)
    val state = Players.varchar("state", length = 10)
    val gender = Players.varchar("gender", length = 5)
    val dateTime = datetime("date_time")

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(playerId, name = "PK_Player_ID")

    fun toPlayer(row: ResultRow, level: Int) = Player(
        playerId = row[playerId],
        username = row[username],
        name = row[name],
        image = row[image],
        level = level,
        state = row[state],
        gender = row[gender]
    )

}
