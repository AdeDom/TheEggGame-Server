package com.adedom.teg.repositories

import com.adedom.teg.db.MapResponse
import com.adedom.teg.db.Players
import com.adedom.teg.request.SignInRequest
import com.adedom.teg.request.SignUpRequest
import com.adedom.teg.route.GetConstant
import com.adedom.teg.util.copyToSuspend
import com.adedom.teg.util.encryptSHA
import com.adedom.teg.util.jwt.PlayerPrincipal
import com.adedom.teg.util.toResourcesPathName
import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime
import java.io.File

class TegRepositoryImpl : TegRepository {

    override fun postSignIn(signInRequest: SignInRequest): Pair<String, PlayerPrincipal?> {
        var message = ""
        var playerPrincipal: PlayerPrincipal? = null
        transaction {
            message = when {
                validateSignIn(signInRequest) == 0 -> "Username and password incorrect"
                else -> {
                    playerPrincipal = signIn(signInRequest)
                    "Post sign in success"
                }
            }
        }
        return Pair(message, playerPrincipal)
    }

    private fun validateSignIn(signInRequest: SignInRequest): Int {
        val (username, password) = signInRequest
        return Players.select {
            Players.username eq username!! and (Players.password eq password.encryptSHA())
        }.count().toInt()
    }

    private fun signIn(signInRequest: SignInRequest): PlayerPrincipal {
        val (username, password) = signInRequest
        return Players.slice(Players.playerId, Players.username)
            .select { Players.username eq username!! and (Players.password eq password.encryptSHA()) }
            .map { MapResponse.toPlayerPrincipal(it) }
            .single()
    }

    override fun postSignUp(signUpRequest: SignUpRequest): PlayerPrincipal {
        val (username, password, name, gender) = signUpRequest
        return transaction {
            Players.insert {
                it[Players.username] = username!!
                it[Players.password] = password.encryptSHA()
                it[Players.name] = name!!.capitalize()
                it[Players.gender] = gender!!
                it[dateTime] = DateTime.now()
            }

            Players.slice(Players.playerId, Players.username)
                .select { Players.username eq username!! and (Players.password eq password.encryptSHA()) }
                .map { MapResponse.toPlayerPrincipal(it) }
                .single()
        }
    }

    override fun validateUsername(username: String) = transaction {
        val count = Players.select { Players.username eq username }
            .count()
            .toInt()

        count == 0
    }

    override fun validateName(name: String): Boolean = transaction {
        val count = Players.select { Players.name eq name }
            .count()
            .toInt()

        count == 0
    }

    override suspend fun changeImageProfile(playerId: Int, multiPartData: MultiPartData) {
        multiPartData.forEachPart { part ->
            if (part.name == GetConstant.IMAGE_FILE && part is PartData.FileItem) {
                val ext = File(part.originalFileName).extension
                val imageName = "image-${System.currentTimeMillis()}.$ext"
                val file = File(imageName.toResourcesPathName())
                part.streamProvider().use { input ->
                    file.outputStream().buffered().use { output ->
                        input.copyToSuspend(output)
                    }
                }
                transaction {
                    Players.update({ Players.playerId eq playerId }) {
                        it[image] = imageName
                    }
                }
            }
            part.dispose()
        }
    }

}