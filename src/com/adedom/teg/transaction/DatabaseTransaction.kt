package com.adedom.teg.transaction

import com.adedom.teg.db.*
import com.adedom.teg.models.*
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
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

    fun fetchItemCollection(): List<ItemCollection> {
        return transaction {
            ItemCollections.selectAll()
                .map { ItemCollections.toItemCollection(it) }
        }
    }

    fun fetchLogActive(): List<LogActive> {
        return transaction {
            LogActives.selectAll()
                .map { LogActives.toLogActive(it) }
        }
    }

    fun fetchMulti(): List<Multi> {
        return transaction {
            Multis.selectAll()
                .map { Multis.toMulti(it) }
        }
    }

    fun fetchMultiCollection(): List<MultiCollection> {
        return transaction {
            MultiCollections.selectAll()
                .map { MultiCollections.toMultiCollection(it) }
        }
    }

    fun fetchRoom(): List<Room> {
        return transaction {
            Rooms.selectAll()
                .map { Rooms.toRoom(it) }
        }
    }

    fun fetchRoomInfo(): List<RoomInfo> {
        return transaction {
            RoomInfos.selectAll()
                .map { RoomInfos.toRoomInfo(it) }
        }
    }

}
