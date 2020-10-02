package com.adedom.teg.service.business

import com.adedom.teg.util.TegConstant
import com.auth0.jwt.JWT
import java.text.SimpleDateFormat
import kotlin.reflect.KProperty0

class TegBusinessImpl : TegBusiness {

    override fun isValidateGender(gender: String): Boolean {
        return gender == TegConstant.GENDER_MALE || gender == TegConstant.GENDER_FEMALE
    }

    override fun isValidateRankPlayer(rankLimit: Int): Boolean {
        return rankLimit == TegConstant.RANK_LIMIT_TEN ||
                rankLimit == TegConstant.RANK_LIMIT_FIFTY ||
                rankLimit == TegConstant.RANK_LIMIT_HUNDRED
    }

    override fun isValidateDateTime(dateTime: String): Boolean {
        return try {
            SimpleDateFormat("dd/MM/yyyy").parse(dateTime)
            false
        } catch (e: Throwable) {
            true
        }
    }

    override fun isValidateState(state: String): Boolean {
        return state == TegConstant.STATE_ONLINE || state == TegConstant.STATE_OFFLINE
    }

    override fun isValidateJWT(token: String, name: String): Boolean {
        return try {
            JWT().decodeJwt(token).getClaim(name).asString() == null
        } catch (e: Throwable) {
            true
        }
    }

    override fun toMessageIsNullOrBlank(values: String?): String {
        return "Please enter $values"
    }

    override fun toMessageIsNullOrBlank(kProperty0: KProperty0<String?>): String {
        return "Please enter ${kProperty0.name}"
    }

    override fun toMessageIsNullOrBlank1(kProperty0: KProperty0<Int?>): String {
        return "Please enter ${kProperty0.name}"
    }

    override fun toMessageIsNullOrBlank2(kProperty0: KProperty0<Double?>): String {
        return "Please enter ${kProperty0.name}"
    }

    override fun toMessageIncorrect(kProperty0: KProperty0<String?>): String {
        return "${kProperty0.name} Incorrect"
    }

    override fun toMessageIncorrect1(kProperty0: KProperty0<Int?>): String {
        return "${kProperty0.name} Incorrect"
    }

    override fun toMessageGender(kProperty0: KProperty0<String?>): String {
        return "${kProperty0.name} incorrect. Please enter ${TegConstant.GENDER_MALE} or ${TegConstant.GENDER_FEMALE}"
    }

    override fun toMessageRepeat(kProperty0: KProperty0<String?>, other: String): String {
        return "${kProperty0.name} repeat. Please enter other $other"
    }

    override fun validateGrateEq(kProperty0: KProperty0<String?>, length: Int): String {
        return "Please enter ${kProperty0.name} a number greater than or equal to $length"
    }

}
