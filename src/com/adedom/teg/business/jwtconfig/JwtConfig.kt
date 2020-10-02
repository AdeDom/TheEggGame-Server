package com.adedom.teg.business.jwtconfig

import com.auth0.jwt.JWTVerifier

interface JwtConfig {

    val verifier: JWTVerifier

    val realm: String

    val playerId: String

    fun makeAccessToken(playerId: String): String

    fun makeRefreshToken(playerId: String): String

    fun decodeJwtGetPlayerId(refreshToken: String): String

}
