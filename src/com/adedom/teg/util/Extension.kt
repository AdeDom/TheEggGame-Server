package com.adedom.teg.util

import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

fun DateTime.toDateFormat() = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(toDate())

fun String?.validateEmpty() = "Please enter $this"

fun String?.validateLessEqZero() = "Please check the $this again"

fun String?.validateNotFound() = "$this not found"

fun String?.validateGender() = (this == "M" || this == "F")

fun String?.validateTeam() = (this == "A" || this == "B")

fun String?.validateIncorrect() = "$this Incorrect"

fun String?.validateRepeatUsername() = "Username is $this repeat other in the egg game"

fun String?.validateRepeatName() = "Name is $this repeat other in the egg game"

infix fun String?.validateGrateEq(length: Int) = "Please enter a number greater than or equal to $length"
