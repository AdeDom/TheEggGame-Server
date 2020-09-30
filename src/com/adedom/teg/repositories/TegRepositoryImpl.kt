package com.adedom.teg.repositories

import com.adedom.teg.controller.account.model.ChangePasswordRequest
import com.adedom.teg.controller.account.model.ChangeProfileRequest
import com.adedom.teg.controller.account.model.StateRequest
import com.adedom.teg.controller.application.model.RankPlayersRequest
import com.adedom.teg.controller.auth.model.SignInRequest
import com.adedom.teg.controller.auth.model.SignUpRequest
import com.adedom.teg.controller.single.model.ItemCollectionRequest
import com.adedom.teg.data.BASE_IMAGE
import com.adedom.teg.db.ItemCollections
import com.adedom.teg.db.LogActives
import com.adedom.teg.db.MapResponse
import com.adedom.teg.db.Players
import com.adedom.teg.models.Backpack
import com.adedom.teg.models.PlayerInfo
import com.adedom.teg.util.TegConstant
import com.adedom.teg.util.toConvertBirthdate
import com.adedom.teg.util.toLevel
import io.ktor.locations.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
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
                Players.username eq username!! and (Players.password eq password.encryptSHA())
            }.count().toInt() == 0
        }
    }

    override fun isValidateChangePassword(playerId: String, changePasswordRequest: ChangePasswordRequest): Boolean {
        val (oldPassword) = changePasswordRequest
        return transaction {
            Players.select {
                Players.playerId eq playerId and (Players.password eq oldPassword.encryptSHA())
            }.count().toInt() == 0
        }
    }

    override fun signIn(signInRequest: SignInRequest): String {
        val (username, password) = signInRequest
        val resultRow = transaction {
            Players.slice(Players.playerId)
                .select { Players.username eq username!! and (Players.password eq password.encryptSHA()) }
                .single()
        }

        // player id
        return resultRow[Players.playerId]
    }

    override fun signUp(signUpRequest: SignUpRequest): Pair<Boolean, String> {
        val (username, password, name, gender, birthdate) = signUpRequest

        val statement = transaction {
            Players.insert {
                it[Players.playerId] = randomUUID()
                it[Players.username] = username!!
                it[Players.password] = password.encryptSHA()
                it[Players.name] = name!!.capitalize()
                it[Players.gender] = gender!!
                it[Players.birthdate] = birthdate.convertBirthdateStringToLong()
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
                it[image] = BASE_IMAGE + imageName
                it[dateTimeUpdated] = System.currentTimeMillis()
            }
        }
        return transaction == 1
    }

    override fun fetchPlayerInfo(playerId: String): PlayerInfo {
        val resultRow = transaction {
            (Players innerJoin ItemCollections).slice(
                Players.playerId,
                Players.username,
                Players.name,
                Players.image,
                ItemCollections.level,
                Players.state,
                Players.gender,
                Players.birthdate,
            ).select { Players.playerId eq playerId }
                .single()
        }

        return PlayerInfo(
            playerId = resultRow[Players.playerId],
            username = resultRow[Players.username],
            name = resultRow[Players.name],
            image = resultRow[Players.image],
            level = resultRow[ItemCollections.level].toLevel(),
            state = resultRow[Players.state],
            gender = resultRow[Players.gender],
            birthdate = resultRow[Players.birthdate].toConvertBirthdate(),
        )
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
                it[password] = newPassword.encryptSHA()
                it[dateTimeUpdated] = System.currentTimeMillis()
            }
        }
        return transaction == 1
    }

    override fun changeProfile(playerId: String, changeProfileRequest: ChangeProfileRequest): Boolean {
        val (name, gender, birthdate) = changeProfileRequest
        val transaction: Int = transaction {
            Players.update({ Players.playerId eq playerId }) {
                it[Players.name] = name!!.capitalize()
                it[Players.gender] = gender!!
                it[Players.birthdate] = birthdate.convertBirthdateStringToLong()
                it[Players.dateTimeUpdated] = System.currentTimeMillis()
            }
        }
        return transaction == 1
    }

    override fun fetchRankPlayers(rankPlayersRequest: RankPlayersRequest): List<PlayerInfo> {
        val (_, search, limit) = rankPlayersRequest

        return transaction {
            addLogger(StdOutSqlLogger)

            val query = (Players innerJoin ItemCollections)
                .slice(
                    Players.playerId,
                    Players.username,
                    Players.name,
                    Players.image,
                    ItemCollections.level,
                    Players.state,
                    Players.gender,
                    Players.birthdate,
                )
                .select { ItemCollections.itemId eq 1 and (Players.name like "%${search}%") }
                .groupBy(Players.playerId)
                .orderBy(ItemCollections.level to SortOrder.DESC, Players.playerId to SortOrder.ASC)

            when (limit!!.toInt()) {
                TegConstant.RANK_LIMIT_TEN -> query.limit(TegConstant.RANK_LIMIT_TEN)
                TegConstant.RANK_LIMIT_FIFTY -> query.limit(TegConstant.RANK_LIMIT_FIFTY)
                TegConstant.RANK_LIMIT_HUNDRED -> query.limit(TegConstant.RANK_LIMIT_HUNDRED)
            }

            query.map { MapResponse.toPlayers(it) }
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
            val resultRow = LogActives.slice(LogActives.logId)
                .select { LogActives.playerId eq playerId }
                .orderBy(LogActives.logId to SortOrder.DESC)
                .limit(1)
                .single()

            val logActiveLogId: Int = resultRow[LogActives.logId]

            LogActives.update({ LogActives.logId eq logActiveLogId }) {
                it[LogActives.dateTimeOut] = System.currentTimeMillis()
            }
        }

        return transaction == 1
    }

    override fun fetchItemCollection(playerId: String): Backpack {
        return transaction {
            addLogger(StdOutSqlLogger)

            val eggI =
                ItemCollections.select { ItemCollections.playerId eq playerId and (ItemCollections.itemId eq TegConstant.SINGLE_ITEM_ONE) }
                    .map { ItemCollections.toItemCollection(it) }
                    .sumBy { it.qty!! }

            val eggII =
                ItemCollections.select { ItemCollections.playerId eq playerId and (ItemCollections.itemId eq TegConstant.SINGLE_ITEM_TWO) }
                    .map { ItemCollections.toItemCollection(it) }
                    .sumBy { it.qty!! }

            val eggIII =
                ItemCollections.select { ItemCollections.playerId eq playerId and (ItemCollections.itemId eq TegConstant.SINGLE_ITEM_THREE) }
                    .map { ItemCollections.toItemCollection(it) }
                    .sumBy { it.qty!! }

            Backpack(eggI, eggII, eggIII)
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

    private fun randomUUID() = UUID.randomUUID().toString().replace("-", "")

    private fun String?.convertBirthdateStringToLong(): Long {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val date = sdf.parse(this)
        return if (date.time < 0L) {
            date.time
        } else {
            val dd = SimpleDateFormat("dd").format(date)
            val MM = SimpleDateFormat("MM").format(date)
            val yyyy = SimpleDateFormat("yyyy").format(date).toInt() - 543
            sdf.parse("$dd/$MM/$yyyy").time
        }
    }

    private fun String?.encryptSHA(): String {
        var sha = ""
        try {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            val byteArray = messageDigest.digest(this?.toByteArray())
            val bigInteger = BigInteger(1, byteArray)
            sha = bigInteger.toString(16).padStart(64, '0')
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return sha
    }

}
