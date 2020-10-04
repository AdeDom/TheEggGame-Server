package com.adedom.teg.business.business

import com.adedom.teg.util.TegConstant
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TegBusinessImplTest {

    private lateinit var business: TegBusiness

    @Before
    fun setup() {
        business = TegBusinessImpl()
    }

    @Test
    fun isValidateGender_genderMale_returnTrue() {
        // given
        val gender = TegConstant.GENDER_MALE

        // when
        val result = business.isValidateGender(gender)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidateGender_genderFemale_returnTrue() {
        // given
        val gender = TegConstant.GENDER_FEMALE

        // when
        val result = business.isValidateGender(gender)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidateGender_genderOtherMale_returnFalse() {
        // given
        val gender = "male"

        // when
        val result = business.isValidateGender(gender)

        // then
        assertFalse(result)
    }

}
