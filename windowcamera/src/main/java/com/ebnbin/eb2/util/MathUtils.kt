package com.ebnbin.eb2.util

/**
 * 最大公约数.
 */
tailrec infix fun Int.gcd(other: Int): Int {
    if (this <= 0 || other <= 0) throw RuntimeException()
    return if (this % other == 0) other else other gcd this % other
}
