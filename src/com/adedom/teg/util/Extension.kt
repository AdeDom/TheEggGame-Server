package com.adedom.teg.util

import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

fun DateTime.toDateFormat() = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(toDate())

fun Double?.isNull() = this == null

fun String?.validateEmpty() = "Please enter $this"

fun String?.validateLessEqZero() = "Please check the $this again"

fun String?.validateNotFound() = "$this not found"

infix fun String?.validateGrateEq(length: Int) = "Please enter a number greater than or equal to $length"
