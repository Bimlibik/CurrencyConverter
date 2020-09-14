package com.foxy.currencyconverter.util
import kotlin.math.round

fun Double.round(): Double {
    var multiplier = 1.0
    repeat(2) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}