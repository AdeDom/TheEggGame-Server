package com.adedom.teg.service.business

interface TegBusiness {

    fun isValidateGender(gender: String): Boolean

    fun isValidateRankPlayer(rankLimit: Int): Boolean

    fun isValidateDateTime(dateTime: String): Boolean

    fun isValidateState(state: String): Boolean

}
