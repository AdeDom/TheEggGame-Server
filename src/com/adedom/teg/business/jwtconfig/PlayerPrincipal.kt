package com.adedom.teg.business.jwtconfig

import io.ktor.auth.Principal

data class PlayerPrincipal(val playerId: String? = null) : Principal
