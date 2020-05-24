package com.adedom.teg.transaction

import com.adedom.teg.db.*
import com.adedom.teg.models.*
import com.adedom.teg.request.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

object DatabaseTransaction {

    fun getCountPlayer(playerId: Int) = transaction {
        Players.select { Players.playerId eq playerId }
            .count()
            .toInt()
    }

    fun getCountRoom(roomNo: String) = transaction {
        Rooms.select { Rooms.roomNo eq roomNo }
            .count()
            .toInt()
    }

    fun getCountRoomInfo(roomNo: String) = transaction {
        RoomInfos.select { RoomInfos.roomNo eq roomNo }
            .count()
            .toInt()
    }

    fun getCountLogActive(logKey: String) = transaction {
        LogActives.select { LogActives.logKey eq logKey }
            .count()
            .toInt()
    }

    fun getCountPasswordPlayer(putPassword: PutPassword): Int {
        val (playerId, oldPassword, _) = putPassword
        return transaction {
            Players.select { Players.playerId eq playerId!! and (Players.password eq oldPassword!!) }
                .count()
                .toInt()
        }
    }

    fun getPlayer(playerId: Int): Player {
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

    fun putLatLng(putLatlng: PutLatlng) {
        val (roomNo, playerId, latitude, longitude) = putLatlng
        transaction {
            RoomInfos.update({
                RoomInfos.roomNo eq roomNo!! and (RoomInfos.playerId eq playerId!!)
            }) {
                it[RoomInfos.latitude] = latitude!!
                it[RoomInfos.longitude] = longitude!!
            }
        }
    }

    fun putReady(putReady: PutReady) {
        val (roomNo, playerId, status) = putReady
        transaction {
            RoomInfos.update({
                RoomInfos.roomNo eq roomNo!! and (RoomInfos.playerId eq playerId!!)
            }) {
                it[RoomInfos.status] = status!!
            }
        }
    }

    fun putRoomOff(roomNo: String) = transaction {
        Rooms.update({ Rooms.roomNo eq roomNo }) {
            it[status] = "off"
        }
    }

    fun putState(putState: PutState) {
        val (playerId, state) = putState
        transaction {
            Players.update({ Players.playerId eq playerId!! }) {
                it[Players.state] = state!!
            }
        }
    }

    fun putTeam(putTeam: PutTeam) {
        val (roomNo, playerId, team) = putTeam
        transaction {
            RoomInfos.update({
                RoomInfos.roomNo eq roomNo!! and (RoomInfos.playerId eq playerId!!)
            }) {
                it[RoomInfos.team] = team!!
            }
        }
    }

    fun putLogActive(logKey: String) {
        transaction {
            LogActives.update({
                LogActives.logKey eq logKey
            }) {
                it[dateTimeOut] = DateTime.now()
            }
        }
    }

    fun putPassword(putPassword: PutPassword) {
        val (playerId, _, newPassword) = putPassword
        transaction {
            Players.update({
                Players.playerId eq playerId!!
            }) {
                it[password] = newPassword!!
            }
        }
    }

    fun putProfile(putProfile: PutProfile) {
        val (playerId, name, gender) = putProfile
        transaction {
            Players.update({
                Players.playerId eq playerId!!
            }) {
                it[Players.name] = name!!
                it[Players.gender] = gender!!
            }
        }
    }

    fun deletePlayerRoomInfo(deletePlayerRoomInfo: DeletePlayerRoomInfo) {
        val (roomNo, playerId) = deletePlayerRoomInfo
        transaction {
            RoomInfos.deleteWhere {
                RoomInfos.roomNo eq roomNo!! and (RoomInfos.playerId eq playerId!!)
            }
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
