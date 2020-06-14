package com.adedom.teg.controller

import com.adedom.teg.route.*
import io.ktor.routing.Route
import io.ktor.routing.route

fun Route.controller() {

    route("application") {
        getPlayers()
        logActive()
    }

    route("account") {
        getPlayer()
        postSignIn()
        postSignUp()
        patchPassword()
        putProfile()
        patchState()
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
