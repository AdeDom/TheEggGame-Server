package com.adedom.teg.data.repositories

import com.adedom.teg.data.database.ItemCollections
import com.adedom.teg.data.database.LogActives
import com.adedom.teg.data.database.Players
import com.adedom.teg.data.map.MapObject
import com.adedom.teg.data.models.*
import com.adedom.teg.models.request.*
import com.adedom.teg.util.TegConstant
import io.ktor.locations.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

@KtorExperimentalLocationsAPI
class TegRepositoryImpl : TegRepository {

    override fun isUsernameRepeat(username: String): Boolean {
        return transaction {
            Players.select { Players.username eq username }
                .count().toInt() > 0
        }
    }

    override fun isNameRepeat(name: String): Boolean {
        return transaction {
            Players.select { Players.name eq name }
                .count().toInt() > 0
        }
    }

    override fun isValidateSignIn(signInRequest: SignInRequest): Boolean {
        val (username, password) = signInRequest
        return transaction {
            Players.select {
                Players.username eq username!! and (Players.password eq password!!)
            }.count().toInt() == 0
        }
    }

    override fun isValidateChangePassword(playerId: String, changePasswordRequest: ChangePasswordRequest): Boolean {
        val (oldPassword) = changePasswordRequest
        return transaction {
            Players.select {
                Players.playerId eq playerId and (Players.password eq oldPassword!!)
            }.count().toInt() == 0
        }
    }

    override fun signIn(signInRequest: SignInRequest): PlayerIdDb {
        val (username, password) = signInRequest
        return transaction {
            Players.slice(Players.playerId)
                .select { Players.username eq username!! and (Players.password eq password!!) }
                .map { MapObject.toPlayerIdDb(it) }
                .single()
        }
    }

    override fun signUp(signUp: SignUpDb): Pair<Boolean, String> {
        val (username, password, name, gender, birthdate) = signUp

        val statement = transaction {
            Players.insert {
                it[Players.playerId] = UUID.randomUUID().toString().replace("-", "")
                it[Players.username] = username!!
                it[Players.password] = password!!
                it[Players.name] = name!!.capitalize()
                it[Players.gender] = gender!!
                it[Players.birthdate] = birthdate!!
                it[Players.dateTimeCreated] = System.currentTimeMillis()
            }
        }

        val resulted = statement.resultedValues?.size == 1
        val playerId = statement.resultedValues?.get(0)?.get(Players.playerId)

        return if (playerId.isNullOrBlank()) Pair(false, "") else Pair(resulted, playerId)
    }

    //    todo resize image
    override fun changeImageProfile(playerId: String, imageName: String): Boolean {
        val transaction: Int = transaction {
            Players.update({ Players.playerId eq playerId }) {
                it[image] = TegConstant.BASE_IMAGE + imageName
                it[dateTimeUpdated] = System.currentTimeMillis()
            }
        }
        return transaction == 1
    }

    override fun fetchPlayerInfo(playerId: String): PlayerInfoDb {
        return transaction {
            (Players innerJoin ItemCollections).slice(
                Players.playerId,
                Players.username,
                Players.name,
                Players.image,
                ItemCollections.qty.sum(),
                Players.state,
                Players.gender,
                Players.birthdate,
            ).select { Players.playerId eq playerId }
                .map { MapObject.toPlayerInfoDb(it) }
                .single()
        }
    }

    override fun playerState(playerId: String, stateRequest: StateRequest): Boolean {
        val (state) = stateRequest
        val transaction: Int = transaction {
            Players.update({ Players.playerId eq playerId }) {
                it[Players.state] = state
            }
        }
        return transaction == 1
    }

    override fun changePassword(playerId: String, changePasswordRequest: ChangePasswordRequest): Boolean {
        val (_, newPassword) = changePasswordRequest
        val transaction: Int = transaction {
            Players.update({ Players.playerId eq playerId }) {
                it[password] = newPassword!!
                it[dateTimeUpdated] = System.currentTimeMillis()
            }
        }
        return transaction == 1
    }

    override fun changeProfile(playerId: String, changeProfile: ChangeProfileDb): Boolean {
        val (name, gender, birthdate) = changeProfile
        val transaction: Int = transaction {
            Players.update({ Players.playerId eq playerId }) {
                it[Players.name] = name!!.capitalize()
                it[Players.gender] = gender!!
                it[Players.birthdate] = birthdate!!
                it[Players.dateTimeUpdated] = System.currentTimeMillis()
            }
        }
        return transaction == 1
    }

    override fun fetchRankPlayers(rankPlayersRequest: RankPlayersRequest): List<PlayerInfoDb> {
        val (_, search, limit) = rankPlayersRequest

        return transaction {
            addLogger(StdOutSqlLogger)

            val query = (Players innerJoin ItemCollections)
                .slice(
                    Players.playerId,
                    Players.username,
                    Players.name,
                    Players.image,
                    ItemCollections.qty.sum(),
                    Players.state,
                    Players.gender,
                    Players.birthdate,
                )
                .select { ItemCollections.itemId eq 1 and (Players.name like "%${search}%") }
                .groupBy(Players.playerId)
                .orderBy(ItemCollections.qty.sum() to SortOrder.DESC, Players.playerId to SortOrder.ASC)

            when (limit!!.toInt()) {
                TegConstant.RANK_LIMIT_TEN -> query.limit(TegConstant.RANK_LIMIT_TEN)
                TegConstant.RANK_LIMIT_FIFTY -> query.limit(TegConstant.RANK_LIMIT_FIFTY)
                TegConstant.RANK_LIMIT_HUNDRED -> query.limit(TegConstant.RANK_LIMIT_HUNDRED)
            }

            query.map { MapObject.toPlayerInfoDb(it) }
        }
    }

    override fun logActiveOn(playerId: String): Boolean {
        val statement = transaction {
            LogActives.insert {
                it[LogActives.playerId] = playerId
                it[LogActives.dateTimeIn] = System.currentTimeMillis()
            }
        }

        return statement.resultedValues?.size == 1
    }

    override fun logActiveOff(playerId: String): Boolean {
        val transaction = transaction {
            val logActive = LogActives.slice(LogActives.logId)
                .select { LogActives.playerId eq playerId }
                .orderBy(LogActives.logId to SortOrder.DESC)
                .limit(1)
                .map { MapObject.toLogActiveLogIdDb(it) }
                .single()

            LogActives.update({ LogActives.logId eq logActive.logId }) {
                it[LogActives.dateTimeOut] = System.currentTimeMillis()
            }
        }

        return transaction == 1
    }

    override fun fetchItemCollection(playerId: String): BackpackDb {
        return transaction {
            addLogger(StdOutSqlLogger)

            val eggI =
                ItemCollections.select { ItemCollections.playerId eq playerId and (ItemCollections.itemId eq TegConstant.SINGLE_ITEM_ONE) }
                    .map { MapObject.toItemCollectionDb(it) }
                    .sumBy { it.qty!! }

            val eggII =
                ItemCollections.select { ItemCollections.playerId eq playerId and (ItemCollections.itemId eq TegConstant.SINGLE_ITEM_TWO) }
                    .map { MapObject.toItemCollectionDb(it) }
                    .sumBy { it.qty!! }

            val eggIII =
                ItemCollections.select { ItemCollections.playerId eq playerId and (ItemCollections.itemId eq TegConstant.SINGLE_ITEM_THREE) }
                    .map { MapObject.toItemCollectionDb(it) }
                    .sumBy { it.qty!! }

            BackpackDb(eggI, eggII, eggIII)
        }
    }

    override fun itemCollection(playerId: String, itemCollectionRequest: ItemCollectionRequest): Boolean {
        val (itemId, qty, latitude, longitude) = itemCollectionRequest

        val statement = transaction {
            ItemCollections.insert {
                it[ItemCollections.playerId] = playerId
                it[ItemCollections.itemId] = itemId!!
                it[ItemCollections.qty] = qty!!
                it[ItemCollections.latitude] = latitude!!
                it[ItemCollections.longitude] = longitude!!
                it[ItemCollections.dateTime] = System.currentTimeMillis()
            }
        }

        return statement.resultedValues?.size == 1
    }

}
