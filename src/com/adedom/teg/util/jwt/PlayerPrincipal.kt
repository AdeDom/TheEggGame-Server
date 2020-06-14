package com.adedom.teg.util.jwt

import io.ktor.auth.Principal

data class PlayerPrincipal(
    val playerId: Int? = null,
    val username: String? = null
) : Principal
