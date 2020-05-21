package com.adedom.teg.models

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

data class Player(
    val playerId: Int? = null,
    val username: String? = null,
    val name: String? = null,
    val image: String? = null,
    val state: String? = null,
    val gender: String? = null
)

object Players : Table(name = "player") {
    val playerId = integer(name = "player_id").autoIncrement()
    val username = varchar("username", length = 50)
    val name = varchar("name", length = 50)
    val image = varchar("image", length = 50)
    val state = varchar("state", length = 10)
    val gender = varchar("gender", length = 5)
    val dateTime = datetime("date_time")

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(playerId, name = "PK_Player_ID")

    fun toPlayer(row: ResultRow) = Player(
        playerId = row[playerId],
        username = row[username],
        name = row[name],
        image = row[image],
        state = row[state],
        gender = row[gender]
    )

}

data class PlayerResponse(
    var player: Player? = null
) : BaseResponse()
