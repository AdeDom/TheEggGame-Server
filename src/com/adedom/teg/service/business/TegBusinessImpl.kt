package com.adedom.teg.service.business

import com.adedom.teg.util.TegConstant
import java.text.SimpleDateFormat

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

}
