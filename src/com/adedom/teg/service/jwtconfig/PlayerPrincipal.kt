package com.adedom.teg.service.jwtconfig

import io.ktor.auth.Principal

data class PlayerPrincipal(val playerId: String? = null) : Principal
