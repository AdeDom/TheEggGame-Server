package com.adedom.teg.business.business

import com.adedom.teg.data.models.SingleItemDb
import com.adedom.teg.models.TegLatLng
import com.adedom.teg.util.TegConstant
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

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

    @Test
    fun isValidateRankPlayer_rankTen_returnTrue() {
        // given
        val rank = TegConstant.RANK_LIMIT_TEN

        // when
        val result = business.isValidateRankPlayer(rank)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidateRankPlayer_rankFifty_returnTrue() {
        // given
        val rank = TegConstant.RANK_LIMIT_FIFTY

        // when
        val result = business.isValidateRankPlayer(rank)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidateRankPlayer_rankHundred_returnTrue() {
        // given
        val rank = TegConstant.RANK_LIMIT_HUNDRED

        // when
        val result = business.isValidateRankPlayer(rank)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidateRankPlayer_rankOther_returnFalse() {
        // given
        val rank = 99

        // when
        val result = business.isValidateRankPlayer(rank)

        // then
        assertFalse(result)
    }

    @Test
    fun isValidateDateTime_dateThai_returnTrue() {
        // given
        val date = "30/10/2537"

        // when
        val result = business.isValidateDateTime(date)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidateDateTime_dateEnglish_returnFalse() {
        // given
        val date = "30/10/1994"

        // when
        val result = business.isValidateDateTime(date)

        // then
        assertFalse(result)
    }

    @Test
    fun isValidateDateTime_dateEnglish_returnTrue() {
        // given
        val date = "english"

        // when
        val result = business.isValidateDateTime(date)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidateState_stateOnline_returnTrue() {
        // given
        val state = TegConstant.STATE_ONLINE

        // when
        val result = business.isValidateState(state)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidateState_stateOffline_returnTrue() {
        // given
        val state = TegConstant.STATE_OFFLINE

        // when
        val result = business.isValidateState(state)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidateState_stateOther_returnFalse() {
        // given
        val state = "standBy"

        // when
        val result = business.isValidateState(state)

        // then
        assertFalse(result)
    }

    @Test
    fun isValidateJwtIncorrect_jwtCorrect_returnFalse() {
        // given
        val token =
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImF1ZCI6InRoZS1lZ2ctZ2FtZSIsInBsYXllcl9pZCI6ImNmMGUwMDA2ZDBlMTQ3MTg5Mjg5N2IwMTViNjAxMWIwIiwiaXNzIjoia3Rvci5pbyIsImV4cCI6MTYwMTkwOTUzOX0.rmlhP5eSr4IA59AGRKA2BpG4dE5ZU0tE5GiOAMmb2i5SmkT2c_pwJT5sNVbdMvtfkY-OHS5u_XO8p18G3ZBDbQ"
        val keyName = "player_id"

        // when
        val result = business.isValidateJwtIncorrect(token, keyName)

        // then
        assertFalse(result)
    }

    @Test
    fun isValidateJwtIncorrect_jwtTokenIncorrect_returnTrue() {
        // given
        val token =
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImF1ZCI6InRoZS1lZ2ctZ2FtZSIsInBsYXllcl9pZCI6ImNmMGUwMDA2ZDBlMTQ3MTg5Mjg5N2IwMTViNjAxMWIwIiwiaXNzIjoia3Rvci5pbyIsImV4cCI6MTYwMTkwOTUzOX0.rmlhP5eSr4IA59AGRKA2BpG4dE5ZU0tE5GiOAMmb2i5SmkT2c_pwJT5sNVbdMvtfkY-OHS5u_XO8p18G3ZBDbQ"
        val keyName = "player_id"

        // when
        val result = business.isValidateJwtIncorrect(token, keyName)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidateJwtIncorrect_jwtKeyNameIncorrect_returnTrue() {
        // given
        val token =
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImF1ZCI6InRoZS1lZ2ctZ2FtZSIsInBsYXllcl9pZCI6ImNmMGUwMDA2ZDBlMTQ3MTg5Mjg5N2IwMTViNjAxMWIwIiwiaXNzIjoia3Rvci5pbyIsImV4cCI6MTYwMTkwOTUzOX0.rmlhP5eSr4IA59AGRKA2BpG4dE5ZU0tE5GiOAMmb2i5SmkT2c_pwJT5sNVbdMvtfkY-OHS5u_XO8p18G3ZBDbQ"
        val keyName = "user_id"

        // when
        val result = business.isValidateJwtIncorrect(token, keyName)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidateJwtExpires_jwtExpires_returnTrue() {
        // given
        val token =
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImF1ZCI6InRoZS1lZ2ctZ2FtZSIsInBsYXllcl9pZCI6ImNmMGUwMDA2ZDBlMTQ3MTg5Mjg5N2IwMTViNjAxMWIwIiwiaXNzIjoia3Rvci5pbyIsImV4cCI6MTYwMTkwOTUzOX0.rmlhP5eSr4IA59AGRKA2BpG4dE5ZU0tE5GiOAMmb2i5SmkT2c_pwJT5sNVbdMvtfkY-OHS5u_XO8p18G3ZBDbQ"

        // when
        val result = business.isValidateJwtExpires(token)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidateJwtExpires_jwtNotExpires_returnFalse() {
        // given
        val token =
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImF1ZCI6InRoZS1lZ2ctZ2FtZSIsInBsYXllcl9pZCI6ImNmMGUwMDA2ZDBlMTQ3MTg5Mjg5N2IwMTViNjAxMWIwIiwiaXNzIjoia3Rvci5pbyIsImV4cCI6MTcwMTgxMzU0N30.ZUINxDz3lQ2_L5jAvUpUanxs_KEQ3JdkoZNhDCfMvQWWdnrTfa67nhDPdwUPUrJ0RanIhSJDE9cz8PINGvORqA"

        // when
        val result = business.isValidateJwtExpires(token)

        // then
        assertFalse(result)
    }

    @Test
    fun isValidateMinUsername_incorrect_returnTrue() {
        // given
        val username = "123"

        // when
        val result = business.isValidateMinUsername(username)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidateMinUsername_correct_returnFalse() {
        // given
        val username = "1234"

        // when
        val result = business.isValidateMinUsername(username)

        // then
        assertFalse(result)
    }

    @Test
    fun isValidateMinPassword_incorrect_returnTrue() {
        // given
        val password = "123"

        // when
        val result = business.isValidateMinPassword(password)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidateMinPassword_correct_returnFalse() {
        // given
        val password = "1234"

        // when
        val result = business.isValidateMinPassword(password)

        // then
        assertFalse(result)
    }

    @Test
    fun isValidateLessThanOrEqualToZero_incorrect_returnTrue() {
        // given
        val num = 0

        // when
        val result = business.isValidateLessThanOrEqualToZero(num)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidateLessThanOrEqualToZero_correct_returnFalse() {
        // given
        val num = 1

        // when
        val result = business.isValidateLessThanOrEqualToZero(num)

        // then
        assertFalse(result)
    }

    @Test
    fun isMissionMode_incorrect_returnFalse() {
        // given
        val mode = "mission_mode"

        // when
        val result = business.isMissionMode(mode)

        // then
        assertFalse(result)
    }

    @Test
    fun isMissionMode_correctMissionDelivery_returnTrue() {
        // given
        val mode = TegConstant.MISSION_DELIVERY

        // when
        val result = business.isMissionMode(mode)

        // then
        assertTrue(result)
    }

    @Test
    fun isMissionMode_correctMissionSingle_returnTrue() {
        // given
        val mode = TegConstant.MISSION_SINGLE

        // when
        val result = business.isMissionMode(mode)

        // then
        assertTrue(result)
    }

    @Test
    fun isMissionMode_correctMissionMulti_returnTrue() {
        // given
        val mode = TegConstant.MISSION_MULTI

        // when
        val result = business.isMissionMode(mode)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidateDateTimeCurrent_incorrect_returnTrue() {
        // given
        val dateTimeLast = 1603609762783

        // when
        val result = business.isValidateDateTimeCurrent(dateTimeLast)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidateDateTimeCurrent_correct_returnFalse() {
        // given
        val dateTimeLast = 1601466777109

        // when
        val result = business.isValidateDateTimeCurrent(dateTimeLast)

        // then
        assertFalse(result)
    }

    @Test
    fun isValidateMissionSingle_incorrectSizeLessThree_returnFalse() {
        // given
        val dateTimeList = listOf(1601466777109, 1601466777109)

        // when
        val result = business.isValidateMissionSingle(dateTimeList)

        // then
        assertFalse(result)
    }

    @Test
    fun isValidateMissionSingle_incorrectDateOld_returnFalse() {
        // given
        val dateTimeList = listOf(1601466777109, 1601466777109, 1601466777109)

        // when
        val result = business.isValidateMissionSingle(dateTimeList)

        // then
        assertFalse(result)
    }

    @Test
    fun isValidateMissionSingle_correct_returnTrue() {
        // given
        val dateTimeList = listOf(1603613803273, 1603613803273, 1603613803273)

        // when
        val result = business.isValidateMissionSingle(dateTimeList)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidateMissionSingle_incorrectPeople1_returnTrue() {
        // given
        val people = 1

        // when
        val result = business.isValidateRoomPeople(people)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidateMissionSingle_incorrectPeople7_returnTrue() {
        // given
        val people = 7

        // when
        val result = business.isValidateRoomPeople(people)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidateMissionSingle_correct_returnFalse() {
        // given
        val people = 2

        // when
        val result = business.isValidateRoomPeople(people)

        // then
        assertFalse(result)
    }

    @Test
    fun isValidateRoomPeople_incorrect_returnFalse() {
        // given
        val team = "AB"

        // when
        val result = business.isValidateTeam(team)

        // then
        assertFalse(result)
    }

    @Test
    fun isValidateRoomPeople_correctTeamA_returnTrue() {
        // given
        val team = TegConstant.TEAM_A

        // when
        val result = business.isValidateTeam(team)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidateRoomPeople_correctTeamB_returnTrue() {
        // given
        val team = TegConstant.TEAM_B

        // when
        val result = business.isValidateTeam(team)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidatePlayMode_incorrect_returnFalse() {
        // given
        val playMode = "teg"

        // when
        val result = business.isValidatePlayMode(playMode)

        // then
        assertFalse(result)
    }

    @Test
    fun isValidatePlayMode_correctPlayModeMain_returnTrue() {
        // given
        val playMode = TegConstant.PLAY_MODE_MAIN

        // when
        val result = business.isValidatePlayMode(playMode)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidatePlayMode_correctPlayModeSingle_returnTrue() {
        // given
        val playMode = TegConstant.PLAY_MODE_SINGLE

        // when
        val result = business.isValidatePlayMode(playMode)

        // then
        assertTrue(result)
    }

    @Test
    fun isValidatePlayMode_correctPlayModeMulti_returnTrue() {
        // given
        val playMode = TegConstant.PLAY_MODE_MULTI

        // when
        val result = business.isValidatePlayMode(playMode)

        // then
        assertTrue(result)
    }

    @Test
    fun convertBirthDateStringToLong_convertEng() {
        // given
        val birthDateEng = "30/10/1994"

        // when
        val resultEng = business.convertBirthDateStringToLong(birthDateEng)

        // then
        assertEquals(783450000000, resultEng)
    }

    @Test
    fun toConvertDateTimeLongToString_convertBirthDate_returnNull() {
        // given
        val birthDate: Long? = null

        // when
        val result = business.toConvertDateTimeLongToString(birthDate)

        // then
        assertEquals("Error", result)
    }

    @Test
    fun toConvertDateTimeLongToString_convertBirthDate() {
        // given
        val birthDate: Long = 783450000000

        // when
        val result = business.toConvertDateTimeLongToString(birthDate)

        // then
        assertEquals("30/10/1994", result)
    }

    @Test
    fun toConvertLevel_level_returnNull() {
        // given
        val level: Int? = null

        // when
        val result = business.toConvertLevel(level)

        // then
        assertEquals(1, result)
    }

    @Test
    fun toConvertLevel_nonLevel_returnLevel() {
        // given
        val level: Int? = 999

        // when
        val result = business.toConvertLevel(level)

        // then
        assertEquals(1, result)
    }

    @Test
    fun toConvertLevel_level_returnLevel() {
        // given
        val level: Int? = 5999

        // when
        val result = business.toConvertLevel(level)

        // then
        assertEquals(5, result)
    }

    @Test
    fun encryptSHA_encryptPassword() {
        // given
        val password = "1234"

        // when
        val result = business.encryptSHA(password)

        // then
        assertEquals("03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4", result)
    }

    @Test
    fun distanceBetween_start() {
        // given
        val startP = TegLatLng(13.6041, 100.6973523)
        val endP = TegLatLng(13.61437, 100.3702932)

        // when
        val result = business.distanceBetween(startP, endP)

        // then
        assertEquals(35336.91635246766, result)
    }

    @Test
    fun distanceBetween_end() {
        // given
        val startP = TegLatLng(13.6041, 100.6973523)
        val endP = TegLatLng(13.603433, 100.7058903)

        // when
        val result = business.distanceBetween(startP, endP)

        // then
        assertEquals(924.9971982818975, result)
    }

    @Test
    fun distanceBetween_top() {
        // given
        val startP = TegLatLng(13.6041, 100.6973523)
        val endP = TegLatLng(19.116848, 99.8469598)

        // when
        val result = business.distanceBetween(startP, endP)

        // then
        assertEquals(619175.1058225221, result)
    }

    @Test
    fun distanceBetween_bottom() {
        // given
        val startP = TegLatLng(13.6041, 100.6973523)
        val endP = TegLatLng(13.523571, 100.7535358)

        // when
        val result = business.distanceBetween(startP, endP)

        // then
        assertEquals(10811.112868471584, result)
    }

    @Test
    fun addSingleItemTimes_near_returnZero() {
        // given
        val currentLatLng = TegLatLng(13.6041, 100.6973523)
        val singleItems = listOf(
            SingleItemDb(latitude = 13.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 13.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 13.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 13.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 13.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 13.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 13.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 13.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 13.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 13.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 13.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 13.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 13.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 13.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 13.6041, longitude = 100.6973523),
        )

        // when
        val result = business.addSingleItemTimes(currentLatLng, singleItems)

        // then
        assertEquals(0, result)
    }

    @Test
    fun addSingleItemTimes_far_returnTen() {
        // given
        val currentLatLng = TegLatLng(13.6041, 100.6973523)
        val singleItems = listOf(
            SingleItemDb(latitude = 14.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 14.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 14.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 14.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 14.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 14.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 14.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 14.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 14.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 14.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 14.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 14.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 14.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 14.6041, longitude = 100.6973523),
            SingleItemDb(latitude = 14.6041, longitude = 100.6973523),
        )

        // when
        val result = business.addSingleItemTimes(currentLatLng, singleItems)

        // then
        assertEquals(10, result)
    }

    @Test
    fun multiPlayerEndGame_scoreTeamAOverScoreTeamB_returnWin() {
        // given
        val scoreTeamA = 3
        val scoreTeamB = 2
        val team = "A"

        // when
        val result = business.multiPlayerEndGame(scoreTeamA, scoreTeamB, team)

        // then
        assertEquals(TegConstant.MULTI_PLAYER_RESULT_WIN, result?.first)
        assertEquals(TegConstant.MULTI_PLAYER_RESULT_LOSE, result?.second)
        assertTrue(result?.third ?: false)
    }

    @Test
    fun multiPlayerEndGame_scoreTeamALessThenScoreTeamB_returnLose() {
        // given
        val scoreTeamA = 2
        val scoreTeamB = 3
        val team = "A"

        // when
        val result = business.multiPlayerEndGame(scoreTeamA, scoreTeamB, team)

        // then
        assertEquals(TegConstant.MULTI_PLAYER_RESULT_LOSE, result?.first)
        assertEquals(TegConstant.MULTI_PLAYER_RESULT_WIN, result?.second)
        assertFalse(result?.third ?: false)
    }

    @Test
    fun multiPlayerEndGame_scoreTeamAEqualScoreTeamB_returnAlways() {
        // given
        val scoreTeamA = 2
        val scoreTeamB = 2
        val team = "A"

        // when
        val result = business.multiPlayerEndGame(scoreTeamA, scoreTeamB, team)

        // then
        assertEquals(TegConstant.MULTI_PLAYER_RESULT_ALWAYS, result?.first)
        assertEquals(TegConstant.MULTI_PLAYER_RESULT_ALWAYS, result?.second)
        assertTrue(result?.third ?: false)
    }

}
