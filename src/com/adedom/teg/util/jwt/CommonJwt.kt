package com.adedom.teg.util.jwt

object CommonJwt {

    const val SUBJECT = "Authentication"
    const val SECRET = "bc162b7210edb9dae67b90"
    const val ISSUER = "ktor.io"
    const val AUDIENCE = "the-egg-game"
    const val REALM = "ktor.io"
    const val VALIDITY_IN_MS = 36_000_00 * 24
    const val PLAYER_ID = "player_id"

}
