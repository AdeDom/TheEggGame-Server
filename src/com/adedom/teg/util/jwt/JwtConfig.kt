package com.adedom.teg.util.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JwtConfig {

    private val algorithm = Algorithm.HMAC512(CommonJwt.SECRET)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(CommonJwt.ISSUER)
        .build()

    fun makeToken(player: PlayerPrincipal): String = JWT.create()
        .withSubject(CommonJwt.SUBJECT)
        .withIssuer(CommonJwt.ISSUER)
        .withAudience(CommonJwt.AUDIENCE)
        .withClaim(CommonJwt.PLAYER_ID, player.playerId)
        .withExpiresAt(getExpiration())
        .sign(algorithm)

    private fun getExpiration() = Date(System.currentTimeMillis() + CommonJwt.VALIDITY_IN_MS)

}
