package com.foxy.currencyconverter.util
import kotlin.math.round

/**
 *  Extension that round a number to 2 decimals
 */

fun Double.round(): Double {
    var multiplier = 1.0
    repeat(2) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}