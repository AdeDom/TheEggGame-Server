package com.adedom.teg.service.jwtconfig

import com.auth0.jwt.JWTVerifier

interface JwtConfig {

    val verifier: JWTVerifier

    val realm: String

    val playerId: String

    fun makeToken(playerId: String): String

}
