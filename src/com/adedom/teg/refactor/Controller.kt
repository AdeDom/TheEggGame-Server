package com.adedom.teg.refactor

import io.ktor.routing.Route
import io.ktor.routing.route

fun Route.headerController() {

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
