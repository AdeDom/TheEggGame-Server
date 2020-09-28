package com.adedom.teg.repositories

import com.adedom.teg.controller.auth.model.SignUpRequest
import com.adedom.teg.controller.auth.model.SignUpResponse
import com.adedom.teg.data.ApiConstant
import com.adedom.teg.data.BASE_IMAGE
import com.adedom.teg.db.ItemCollections
import com.adedom.teg.db.LogActives
import com.adedom.teg.db.MapResponse
import com.adedom.teg.db.Players
import com.adedom.teg.models.Backpack
import com.adedom.teg.models.Player
import com.adedom.teg.request.account.ChangePasswordRequest
import com.adedom.teg.request.account.ChangeProfileRequest
import com.adedom.teg.request.account.StateRequest
import com.adedom.teg.request.application.LogActiveRequest
import com.adedom.teg.request.application.RankPlayersRequest
import com.adedom.teg.request.auth.SignInRequest
import com.adedom.teg.request.single.ItemCollectionRequest
import com.adedom.teg.response.*
import com.adedom.teg.route.GetConstant
import com.adedom.teg.util.TegConstant
import com.adedom.teg.util.encryptSHA
import com.adedom.teg.util.jwt.JwtConfig
import com.adedom.teg.util.jwt.PlayerPrincipal
import com.adedom.teg.util.validateRepeatName
import com.adedom.teg.util.validateRepeatUsername
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.content.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.io.File
import java.util.*

class TegRepositoryImpl : TegRepository {

    override fun isUsernameRepeat(): Boolean {
        return false
    }

    override fun postSignIn(signInRequest: SignInRequest): SignInResponse {
        val response = SignInResponse()
        val (username, password) = signInRequest

        val isValidateSignIn: Boolean = transaction {
            Players.select {
                Players.username eq username!! and (Players.password eq password.encryptSHA())
            }.count().toInt() == 0
        }

        if (isValidateSignIn) {
            response.message = "Username and password incorrect"
        } else {
            response.success = true
            response.message = "Post sign in success"
            response.accessToken = JwtConfig.makeToken(signIn(signInRequest))
        }

        return response
    }

    private fun signIn(signInRequest: SignInRequest): PlayerPrincipal {
        val (username, password) = signInRequest
        return transaction {
            Players.slice(Players.playerId)
                .select { Players.username eq username!! and (Players.password eq password.encryptSHA()) }
                .map { MapResponse.toPlayerPrincipal(it) }
                .single()
        }
    }

    override fun postSignUp(signUpRequest: SignUpRequest): SignUpResponse {
        val response = SignUpResponse()
        val (username, password, name, gender) = signUpRequest

        val isValidateUsername: Boolean = transaction {
            Players.select { Players.username eq username!! }
                .count().toInt() == 0
        }

        val isValidateName: Boolean = transaction {
            Players.select { Players.name eq name!! }
                .count().toInt() == 0
        }

        when {
            !isValidateUsername -> response.message = username.validateRepeatUsername()
            !isValidateName -> response.message = name.validateRepeatName()
            else -> {
                transaction {
                    Players.insert {
                        it[Players.username] = username!!
                        it[Players.password] = password.encryptSHA()
                        it[Players.name] = name!!.capitalize()
                        it[Players.gender] = gender!!
                        it[dateTimeCreated] = System.currentTimeMillis()
                    }
                }
                response.success = true
                response.message = "Post sign up success"
                response.accessToken = JwtConfig.makeToken(signIn(SignInRequest(username, password)))
            }
        }

        return response
    }

    //    todo resize image
    override suspend fun changeImageProfile(playerId: String, multiPartData: MultiPartData): BaseResponse {
        val response = BaseResponse()
        multiPartData.forEachPart { part ->
            response.message = if (part.name == GetConstant.IMAGE_FILE && part is PartData.FileItem) {
                val username = transaction {
                    Players.slice(Players.username)
                        .select { Players.playerId eq playerId }
                        .map { it[Players.username] }
                        .single()
                }
                val ext = File(part.originalFileName!!).extension
                val imageName = "image-$username.$ext"

//                val file = File(imageName.toResourcesPathName())
//                part.streamProvider().use { input ->
//                    file.outputStream().buffered().use { output ->
//                        input.copyToSuspend(output)
//                    }
//                }

                val byteArray = part.streamProvider().readBytes()
                val encodeToString = Base64.getEncoder().encodeToString(byteArray)
                val httpResponse = HttpClient(Apache).post<HttpResponse> {
                    url("${BASE_IMAGE}/upload-image.php")
                    body = MultiPartFormDataContent(formData {
                        append(ApiConstant.name, imageName)
                        append(ApiConstant.image, encodeToString)
                    })
                }

                val baseResponse: BaseResponse = Gson().fromJson(httpResponse.readText(), BaseResponse::class.java)
                if (baseResponse.success) {
                    val transaction: Int = transaction {
                        Players.update({ Players.playerId eq playerId }) {
                            it[image] = imageName
                        }
                    }
                    if (transaction == 1) response.success = baseResponse.success
                }
                baseResponse.message
            } else {
                "Not found image file"
            }
            part.dispose()
        }
        return response
    }

    override fun fetchPlayerInfo(playerId: String): PlayerResponse {
        val response = PlayerResponse()
        val playerInfo: Player = transaction {
            (Players innerJoin ItemCollections).slice(
                Players.playerId,
                Players.username,
                Players.name,
                Players.image,
                ItemCollections.level,
                Players.state,
                Players.gender
            ).select { Players.playerId eq playerId }
                .map { MapResponse.toPlayer(it) }
                .single()
        }

        response.success = true
        response.message = "Fetch player success"
        response.playerInfo = playerInfo

        return response
    }

    override fun playerState(playerId: String, stateRequest: StateRequest): BaseResponse {
        val response = BaseResponse()
        val (state) = stateRequest
        val transaction: Int = transaction {
            Players.update({ Players.playerId eq playerId }) {
                it[Players.state] = state
            }
        }
        if (transaction == 1) {
            response.success = true
            response.message = "Patch state success"
        }
        return response
    }

    override fun changePassword(playerId: String, changePasswordRequest: ChangePasswordRequest): BaseResponse {
        val (oldPassword, newPassword) = changePasswordRequest
        val response = BaseResponse()

        val isValidatePassword: Boolean = transaction {
            Players.select {
                Players.playerId eq playerId and (Players.password eq oldPassword.encryptSHA())
            }.count().toInt() == 0
        }

        if (isValidatePassword) {
            response.message = "Password incorrect"
        } else {
            val transaction: Int = transaction {
                Players.update({ Players.playerId eq playerId }) {
                    it[password] = newPassword.encryptSHA()
                }
            }
            if (transaction == 1) {
                response.success = true
                response.message = "Change password success"
            }
        }
        return response
    }

    override fun changeProfile(playerId: String, changeProfileRequest: ChangeProfileRequest): BaseResponse {
        val response = BaseResponse()
        val (name, gender) = changeProfileRequest
        val transaction: Int = transaction {
            Players.update({ Players.playerId eq playerId }) {
                it[Players.name] = name!!.capitalize()
                it[Players.gender] = gender!!
            }
        }
        if (transaction == 1) {
            response.success = true
            response.message = "Change profile success"
        }
        return response
    }

    override fun fetchRankPlayers(rankPlayersRequest: RankPlayersRequest): RankPlayersResponse {
        val (_, search, limit) = rankPlayersRequest
        val response = RankPlayersResponse()

        val rankPlayers: List<Player> = transaction {
            addLogger(StdOutSqlLogger)

            val query = (Players innerJoin ItemCollections)
                .slice(
                    Players.playerId,
                    Players.username,
                    Players.name,
                    Players.image,
                    ItemCollections.level,
                    Players.state,
                    Players.gender
                )
                .select { ItemCollections.itemId eq 1 and (Players.name like "%${search}%") }
                .groupBy(Players.playerId)
                .orderBy(ItemCollections.level to SortOrder.DESC, (Players.playerId to SortOrder.ASC))

            when (limit?.toInt()) {
                TegConstant.LIMIT_TEN -> query.limit(TegConstant.LIMIT_TEN)
                TegConstant.LIMIT_FIFTY -> query.limit(TegConstant.LIMIT_FIFTY)
                TegConstant.LIMIT_ONE_HUNDRED -> query.limit(TegConstant.LIMIT_ONE_HUNDRED)
            }

            query.map { MapResponse.toPlayers(it) }
        }

        response.success = true
        response.message = "Fetch rank players success"
        response.rankPlayers = rankPlayers

        return response
    }

    override fun postLogActive(playerId: String, logActiveRequest: LogActiveRequest): BaseResponse {
        val (flagLogActive) = logActiveRequest
        val response = BaseResponse()

        if (flagLogActive == 1) {
            transaction {
                LogActives.insert {
                    it[LogActives.playerId] = playerId
                    it[dateTimeIn] = DateTime.now()
                }
            }
        } else if (flagLogActive == 0) {
            transaction {
                val logActive = LogActives.slice(LogActives.logId)
                    .select { LogActives.playerId eq playerId }
                    .orderBy(LogActives.logId to SortOrder.DESC)
                    .limit(1)
                    .map { MapResponse.toLogActiveId(it) }
                    .single()

                LogActives.update({ LogActives.logId eq logActive.logId!! }) {
                    it[dateTimeOut] = DateTime.now()
                }
            }
        }

        response.success = true
        response.message = "Log active success"

        return response
    }

    override fun fetchItemCollection(playerId: String): BackpackResponse {
        val response = BackpackResponse()

        val backpack: Backpack = transaction {
            val eggI = ItemCollections.select { ItemCollections.playerId eq playerId and (ItemCollections.itemId eq 2) }
                .map { ItemCollections.toItemCollection(it) }
                .sumBy { it.qty!! }

            val eggII =
                ItemCollections.select { ItemCollections.playerId eq playerId and (ItemCollections.itemId eq 3) }
                    .map { ItemCollections.toItemCollection(it) }
                    .sumBy { it.qty!! }

            val eggIII =
                ItemCollections.select { ItemCollections.playerId eq playerId and (ItemCollections.itemId eq 4) }
                    .map { ItemCollections.toItemCollection(it) }
                    .sumBy { it.qty!! }

            Backpack(eggI, eggII, eggIII)
        }

        response.success = true
        response.message = "Fetch item collection success"
        response.backpack = backpack

        return response
    }

    override fun postItemCollection(playerId: String, itemCollectionRequest: ItemCollectionRequest): BaseResponse {
        val response = BaseResponse()
        val (itemId, qty, latitude, longitude) = itemCollectionRequest

        transaction {
            ItemCollections.insert {
                it[ItemCollections.playerId] = playerId
                it[ItemCollections.itemId] = itemId!!
                it[ItemCollections.qty] = qty!!
                it[ItemCollections.latitude] = latitude!!
                it[ItemCollections.longitude] = longitude!!
                it[dateTime] = DateTime.now()
            }
        }

        response.success = true
        response.message = "Post item collection success"

        return response
    }

}
