package com.dara.unitconverter.utils

/**
 * Helper function to validate user input before performing conversions.
 */
fun isValidAmount(input: String): Pair<Boolean, String> {
    return when {
        input.toFloatOrNull() == null -> Pair(false, "Please enter a valid amount")
        else -> Pair(true, "")
    }
}
