package com.adedom.teg.service

import com.adedom.teg.models.Player
import com.adedom.teg.repositories.TegRepository
import com.adedom.teg.request.account.ChangePasswordRequest
import com.adedom.teg.request.account.ChangeProfileRequest
import com.adedom.teg.request.account.ImageProfile
import com.adedom.teg.request.application.LogActiveRequest
import com.adedom.teg.request.application.RankPlayersRequest
import com.adedom.teg.request.auth.SignInRequest
import com.adedom.teg.request.auth.SignUpRequest
import com.adedom.teg.response.BaseResponse
import com.adedom.teg.response.RankPlayersResponse
import com.adedom.teg.util.jwt.PlayerPrincipal
import io.ktor.http.content.MultiPartData

class TegServiceImpl(private val repository: TegRepository) : TegService {

    override fun signIn(signInRequest: SignInRequest): Pair<String, PlayerPrincipal?> {
        return repository.postSignIn(signInRequest)
    }

    override fun signUp(signUpRequest: SignUpRequest): Pair<String, PlayerPrincipal?> {
        return repository.postSignUp(signUpRequest)
    }

    override suspend fun changeImageProfile(playerId: Int, multiPartData: MultiPartData): Pair<String, ImageProfile?> {
        return repository.changeImageProfile(playerId, multiPartData)
    }

    override fun fetchPlayerInfo(playerId: Int): Pair<String, Player?> {
        return repository.fetchPlayerInfo(playerId)
    }

    override fun playerState(playerId: Int, state: String): BaseResponse {
        return repository.playerState(playerId, state)
    }

    override fun changePassword(playerId: Int, changePasswordRequest: ChangePasswordRequest): BaseResponse {
        return repository.changePassword(playerId, changePasswordRequest)
    }

    override fun changeProfile(playerId: Int, changeProfileRequest: ChangeProfileRequest): BaseResponse {
        return repository.changeProfile(playerId, changeProfileRequest)
    }

    override fun fetchRankPlayers(rankPlayersRequest: RankPlayersRequest): RankPlayersResponse {
        return repository.fetchRankPlayers(rankPlayersRequest)
    }

    override fun postLogActive(playerId: Int, logActiveRequest: LogActiveRequest): BaseResponse {
        return repository.postLogActive(playerId, logActiveRequest)
    }

}
