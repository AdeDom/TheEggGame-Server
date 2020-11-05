package com.adedom.teg.util

import com.adedom.teg.business.jwtconfig.PlayerPrincipal
import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.statement.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.channels.ClosedSendChannelException

fun String?.validateIsNullOrBlank() = "Please enter $this"

fun String?.validateLessEqZero() = "Please check the $this again"

fun String?.validateNotFound() = "$this not found"

fun String?.validateTeam() = (this == TegConstant.TEAM_A || this == TegConstant.TEAM_B)

fun String?.validateIncorrect() = "$this Incorrect"

suspend inline fun <reified T> HttpResponse.fromJson(): T = Gson().fromJson(this.readText(), T::class.java)

inline fun <reified T> Frame.fromJson(): T = Gson().fromJson((this as Frame.Text).readText(), T::class.java)

fun <T> T.toJson(): Frame.Text = Frame.Text(Gson().toJson(this))

suspend fun List<WebSocketSession>.send(frame: Frame) {
    forEach {
        try {
            it.send(frame.copy())
        } catch (t: Throwable) {
            try {
                it.close(CloseReason(CloseReason.Codes.PROTOCOL_ERROR, ""))
            } catch (ignore: ClosedSendChannelException) {
                // at some point it will get closed
            }
        }
    }
}

val ApplicationCall.playerId
    get() = authentication.principal<PlayerPrincipal>()?.playerId
