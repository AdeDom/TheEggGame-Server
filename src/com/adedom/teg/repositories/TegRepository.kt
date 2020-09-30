package com.adedom.teg.repositories

import com.adedom.teg.controller.account.model.ChangePasswordRequest
import com.adedom.teg.controller.account.model.ChangeProfileRequest
import com.adedom.teg.controller.account.model.StateRequest
import com.adedom.teg.controller.application.model.RankPlayersRequest
import com.adedom.teg.controller.auth.model.SignInRequest
import com.adedom.teg.controller.auth.model.SignUpRequest
import com.adedom.teg.controller.single.model.ItemCollectionRequest
import com.adedom.teg.models.Backpack
import com.adedom.teg.models.PlayerInfo
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
