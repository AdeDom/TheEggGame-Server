package com.adedom.teg.controller

import com.adedom.teg.route.*
import io.ktor.routing.Route
import io.ktor.routing.route

fun Route.controller() {

    route("application"){
        putLogActive()
    }

    route("account") {
        getPlayer()
        putPassword()
        putState()
    }

    route("fetch-list") {
        fetchList()
    }

    route("multi") {
        putLatlng()
        putReady()
        putRoomOff()
        putTeam()
        deletePlayerRoomInfo()
    }

}
