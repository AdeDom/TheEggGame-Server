package com.adedom.teg.repositories

import com.adedom.teg.db.MapResponse
import com.adedom.teg.db.Players
import com.adedom.teg.request.SignInRequest
import com.adedom.teg.request.SignUpRequest
import com.adedom.teg.util.encryptSHA
import com.adedom.teg.util.jwt.PlayerPrincipal
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class AuthRepository {

    fun postSignIn(signInRequest: SignInRequest): PlayerPrincipal {
        val (username, password) = signInRequest
        return transaction {
            Players.slice(Players.playerId, Players.username)
                .select { Players.username eq username!! and (Players.password eq password.encryptSHA()) }
                .map { MapResponse.toPlayerPrincipal(it) }
                .single()
        }
    }

    fun postSignUp(signUpRequest: SignUpRequest): PlayerPrincipal {
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

}
