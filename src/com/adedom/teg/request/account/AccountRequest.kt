package com.adedom.teg.request.account

import io.ktor.locations.Location

@Location("/api/account/image-profile")
class ImageProfile

@Location("/api/account/player-info")
class PlayerInfo

@Location("/api/account/state/{state}")
data class StateRequest(val state: String? = null)
