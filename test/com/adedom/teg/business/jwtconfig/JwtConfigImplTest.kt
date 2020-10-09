package com.adedom.teg.business.jwtconfig

import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class JwtConfigImplTest {

    private lateinit var jwtConfig: JwtConfig

    @Before
    fun setup() {
        jwtConfig = JwtConfigImpl()
    }

    @Test
    fun realm() {
        // given

        // when
        val result = jwtConfig.realm

        // then
        assertEquals("ktor.io", result)
    }

    @Test
    fun playerId() {
        // given

        // when
        val result = jwtConfig.playerId

        // then
        assertEquals("player_id", result)
    }

    @Test
    fun makeAccessToken() {
        // given
        val playerId = "c00e32d08f5c47d08f6968067cc5edd4"

        // when
        val result = jwtConfig.makeAccessToken(playerId)

        // then
        assertNotNull(result)
    }

    @Test
    fun makeRefreshToken() {
        // given
        val playerId = "c00e32d08f5c47d08f6968067cc5edd4"

        // when
        val result = jwtConfig.makeRefreshToken(playerId)

        // then
        assertNotNull(result)
    }

    @Test
    fun decodeJwtGetPlayerId_accessToken() {
        // given
        val playerId = "c00e32d08f5c47d08f6968067cc5edd4"
        val accessToken = jwtConfig.makeAccessToken(playerId)

        // when
        val result = jwtConfig.decodeJwtGetPlayerId(accessToken)

        // then
        assertEquals(playerId, result)
    }

    @Test
    fun decodeJwtGetPlayerId_refreshToken() {
        // given
        val playerId = "c00e32d08f5c47d08f6968067cc5edd4"
        val refreshToken = jwtConfig.makeRefreshToken(playerId)

        // when
        val result = jwtConfig.decodeJwtGetPlayerId(refreshToken)

        // then
        assertEquals(playerId, result)
    }

}
