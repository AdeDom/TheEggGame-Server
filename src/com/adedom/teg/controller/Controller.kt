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
        postSignIn()
        postSignUp()
        putPassword()
        putProfile()
        putState()
    }

    route("fetch-list") {
        fetchList()
    }

    route("single") {
        itemCollection()
    }

    route("multi") {
        postRoom()
        postRoomInfo()
        multi()
        postMultiCollection()
        putLatlng()
        putReady()
        putRoomOff()
        putTeam()
        deletePlayerRoomInfo()
    }

}
