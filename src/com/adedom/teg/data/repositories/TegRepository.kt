package com.adedom.teg.data.repositories

import com.adedom.teg.data.models.*
import com.adedom.teg.models.request.*
import io.ktor.locations.*

@KtorExperimentalLocationsAPI
interface TegRepository {

    // if repeat return ture
    fun isUsernameRepeat(username: String): Boolean

    // if repeat return ture
    fun isNameRepeat(name: String): Boolean

    // if correct return true
    fun isValidateSignIn(signInRequest: SignInRequest): Boolean

    // if incorrect return true
    fun isValidateChangePassword(playerId: String, changePasswordRequest: ChangePasswordRequest): Boolean

    fun getMissionDateTimeLast(playerId: String, missionRequest: MissionRequest): Long

    fun signIn(signInRequest: SignInRequest): PlayerIdDb

    fun signUp(signUp: SignUpDb): Pair<Boolean, String>

    fun changeImageProfile(playerId: String, imageName: String): Boolean

    fun fetchPlayerInfo(playerId: String): PlayerInfoDb

    fun playerState(playerId: String, stateRequest: StateRequest): Boolean

    fun changePassword(playerId: String, changePasswordRequest: ChangePasswordRequest): Boolean

    fun changeProfile(playerId: String, changeProfile: ChangeProfileDb): Boolean

    fun fetchRankPlayers(rankPlayersRequest: RankPlayersRequest): List<PlayerInfoDb>

    fun logActiveOn(playerId: String): Boolean

    fun logActiveOff(playerId: String): Boolean

    fun fetchItemCollection(playerId: String): BackpackDb

    fun itemCollection(playerId: String, itemCollectionRequest: ItemCollectionRequest): Boolean

    fun missionMain(playerId: String, missionRequest: MissionRequest): Boolean

}
