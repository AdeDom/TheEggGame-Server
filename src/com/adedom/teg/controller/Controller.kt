package com.adedom.teg.controller

import com.adedom.teg.route.*
import com.adedom.teg.service.TegService
import io.ktor.routing.Route
import io.ktor.routing.route

fun Route.connectionController(service: TegService) {

    route("account") {
        authRoute(service)
    }

    route("test") {
        testRoute(service)
    }

}

fun Route.headerController(service: TegService) {
    route("application") {
        getPlayers()
        logActive()
    }

    route("account") {
        getPlayer()
        patchPassword()
        putProfile()
        patchImageProfile(service)
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
