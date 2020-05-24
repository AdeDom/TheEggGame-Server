package com.adedom.teg.transaction

import com.adedom.teg.db.ItemCollections
import com.adedom.teg.db.Players
import com.adedom.teg.models.Player
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseTransaction {

    fun getCountPlayer(playerId: Int): Int {
        return transaction {
            Players.select { Players.playerId eq playerId }
                .count()
                .toInt()
        }
    }

    fun getPlayerInfo(playerId: Int): Player {
        val level = transaction {
            ItemCollections.select { ItemCollections.playerId eq playerId }
                .andWhere { ItemCollections.itemId eq 1 }
                .map { ItemCollections.toItemCollection(it) }
                .sumBy { it.qty!! }
                .div(1000)
        }
        return transaction {
            Players.select { Players.playerId eq playerId }
                .map { Players.toPlayer(it, level) }
                .single()
        }
    }

}
