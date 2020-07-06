package com.adedom.teg.controller

import com.adedom.teg.route.*
import io.ktor.routing.Route
import io.ktor.routing.route

fun Route.headerController() {
    route("application") {
        getPlayers()
        logActive()
    }

    route("account") {
        patchPassword()
        putProfile()
    }

    route("single") {
        itemCollection()
    }

    route("multi") {
        getMultiScore()
        room()
        roomInfo()
        multi()
        postMultiCollection()
        putLatlng()
        putReady()
        putRoomOff()
        putTeam()
    }
}
