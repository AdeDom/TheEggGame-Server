package com.adedom.teg.data.repositories

import com.adedom.teg.models.models.Backpack
import com.adedom.teg.models.models.ChangeProfileItem
import com.adedom.teg.models.models.PlayerInfoDb
import com.adedom.teg.models.models.SignUpItem
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

    fun signIn(signInRequest: SignInRequest): String

    fun signUp(signUpItem: SignUpItem): Pair<Boolean, String>

    fun changeImageProfile(playerId: String, imageName: String): Boolean

    fun fetchPlayerInfo(playerId: String): PlayerInfoDb

    fun playerState(playerId: String, stateRequest: StateRequest): Boolean

    fun changePassword(playerId: String, changePasswordRequest: ChangePasswordRequest): Boolean

    fun changeProfile(playerId: String, changeProfileItem: ChangeProfileItem): Boolean

    fun fetchRankPlayers(rankPlayersRequest: RankPlayersRequest): List<PlayerInfoDb>

    fun logActiveOn(playerId: String): Boolean

    fun logActiveOff(playerId: String): Boolean

    fun fetchItemCollection(playerId: String): Backpack

    fun itemCollection(playerId: String, itemCollectionRequest: ItemCollectionRequest): Boolean

}
