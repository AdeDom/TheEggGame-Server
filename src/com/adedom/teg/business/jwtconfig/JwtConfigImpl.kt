package com.adedom.teg.business.jwtconfig

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

    override fun makeAccessToken(playerId: String): String {
        return encodeJWTSetPlayerId(playerId, Date(System.currentTimeMillis() + ACCESS_TOKEN))
    }

    override fun makeRefreshToken(playerId: String): String {
        return encodeJWTSetPlayerId(playerId, Date(System.currentTimeMillis() + REFRESH_TOKEN))
    }

    private fun encodeJWTSetPlayerId(playerId: String, withExpiresAt: Date): String = JWT.create()
        .withSubject(SUBJECT)
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .withClaim(PLAYER_ID, playerId)
        .withExpiresAt(withExpiresAt)
        .sign(algorithm)

    override fun decodeJwtGetPlayerId(refreshToken: String): String {
        return JWT().decodeJwt(refreshToken).getClaim(PLAYER_ID).asString()
    }

    // TODO: 02/10/2563 access token 60 second
    companion object {
        const val SUBJECT = "Authentication"
        const val SECRET = "bc162b7210edb9dae67b90"
        const val ISSUER = "ktor.io"
        const val AUDIENCE = "the-egg-game"
        const val ACCESS_TOKEN = 60_000
        const val REFRESH_TOKEN = 36_000_00 * 24 * 7
        const val PLAYER_ID = "player_id"
        const val REALM = "ktor.io"
    }

}
