package com.adedom.teg.service.jwtconfig

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JwtConfigImpl : JwtConfig {

    private val algorithm = Algorithm.HMAC512(SECRET)

    override val verifier: JWTVerifier
        get() = JWT
            .require(algorithm)
            .withIssuer(ISSUER)
            .build()

    override val realm: String
        get() = REALM

    override val playerId: String
        get() = PLAYER_ID

    override fun makeToken(playerId: String): String = JWT.create()
        .withSubject(SUBJECT)
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .withClaim(PLAYER_ID, playerId)
        .withExpiresAt(getExpiration())
        .sign(algorithm)

    private fun getExpiration() = Date(System.currentTimeMillis() + VALIDITY_IN_MS)

    companion object {
        const val SUBJECT = "Authentication"
        const val SECRET = "bc162b7210edb9dae67b90"
        const val ISSUER = "ktor.io"
        const val AUDIENCE = "the-egg-game"
        const val VALIDITY_IN_MS = 36_000_00 * 24
        const val PLAYER_ID = "player_id"
        const val REALM = "ktor.io"
    }

}
