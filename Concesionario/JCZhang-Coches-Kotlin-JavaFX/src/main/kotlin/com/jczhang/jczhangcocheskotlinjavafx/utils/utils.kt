package com.jczhang.jczhangcocheskotlinjavafx.utils

import java.time.LocalDate

fun localDateFromString(input: String): LocalDate {

    val campos  = input.split("-")
    return LocalDate.of(campos[0].toInt(), campos[1].toInt(), campos[2].toInt())
}