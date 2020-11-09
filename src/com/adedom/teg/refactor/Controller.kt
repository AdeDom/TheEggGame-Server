package com.adedom.teg.refactor

import io.ktor.routing.*

fun Route.headerController() {

    route("multi") {
        getMultiScore()
        roomInfo()
        multi()
        postMultiCollection()
        putLatlng()
        putReady()
        putRoomOff()
        putTeam()
    }

}
