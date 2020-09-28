package com.adedom.teg.util.jwt

import io.ktor.application.ApplicationCall
import io.ktor.auth.authentication

val ApplicationCall.player get() = authentication.principal<PlayerPrincipal>()

val ApplicationCall.playerId get() = authentication.principal<PlayerPrincipal>()?.playerId
