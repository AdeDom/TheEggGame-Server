package com.adedom.teg.controller

import com.adedom.teg.route.*
import io.ktor.routing.Route
import io.ktor.routing.route

fun Route.controller() {

    route("application") {
        putLogActive()
    }

    route("account") {
        getPlayer()
        postSignUp()
        putPassword()
        putProfile()
        putState()
    }

    route("fetch-list") {
        fetchList()
    }

    route("single") {
        postItemCollection()
    }

    route("multi") {
        putLatlng()
        putReady()
        putRoomOff()
        putTeam()
        deletePlayerRoomInfo()
    }

}
