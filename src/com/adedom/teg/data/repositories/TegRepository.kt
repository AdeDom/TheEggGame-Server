package com.adedom.teg.data.repositories

import com.adedom.teg.http.models.request.ChangePasswordRequest
import com.adedom.teg.http.models.request.ChangeProfileRequest
import com.adedom.teg.http.models.request.StateRequest
import com.adedom.teg.http.models.request.RankPlayersRequest
import com.adedom.teg.http.models.request.SignInRequest
import com.adedom.teg.http.models.request.SignUpRequest
import com.adedom.teg.http.models.request.ItemCollectionRequest
import com.adedom.teg.refactor.Backpack
import com.adedom.teg.refactor.PlayerInfo
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

    fun signIn(signInRequest: SignInRequest): String

    fun signUp(signUpRequest: SignUpRequest): Pair<Boolean, String>

    fun changeImageProfile(playerId: String, imageName: String): Boolean

    fun fetchPlayerInfo(playerId: String): PlayerInfo

    fun playerState(playerId: String, stateRequest: StateRequest): Boolean

    fun changePassword(playerId: String, changePasswordRequest: ChangePasswordRequest): Boolean

    fun changeProfile(playerId: String, changeProfileRequest: ChangeProfileRequest): Boolean

    fun fetchRankPlayers(rankPlayersRequest: RankPlayersRequest): List<PlayerInfo>

    fun logActiveOn(playerId: String): Boolean

    fun logActiveOff(playerId: String): Boolean

    fun fetchItemCollection(playerId: String): Backpack

    fun itemCollection(playerId: String, itemCollectionRequest: ItemCollectionRequest): Boolean

}
