package com.dara.unitconverter.utils

/**
 * A utility object for performing various unit conversions.
 *
 * This object provides nested objects for different categories of units, such as
 * temperature, length, and mass. Each nested object contains functions to convert
 * between various units within that category.
 */
object UnitConverter {

    /**
     * Checks if a given amount string is valid.
     *
     * An amount is considered valid if it is not "0" and can be successfully parsed as a Float.
     *
     * @param amountFrom The string representing the amount to validate.
     * @return A [Pair] where the first element is a [Boolean] indicating the validity of the amount
     *          (true if valid, false otherwise), and the second element is a [String] containing
     *          an error message if the amount is invalid (empty string if valid).
     */
    fun isValidAmount(amountFrom: String): Pair<Boolean, String> {
        return when {
            amountFrom == "0" -> Pair(false, "Amount cannot be zero")
            amountFrom.toFloatOrNull() == null -> Pair(false, "Please enter a valid amount")
            else -> Pair(true, "")
        }
    }

    // Temperature conversions
    object Temperature {
        fun celsiusToFahrenheit(c: Double) = (c * 9 / 5) + 32
        fun fahrenheitToCelsius(f: Double) = (f - 32) * 5 / 9

        fun celsiusToKelvin(c: Double) = c + 273.15
        fun kelvinToCelsius(k: Double) = k - 273.15

        fun fahrenheitToKelvin(f: Double) = celsiusToKelvin(fahrenheitToCelsius(f))
        fun kelvinToFahrenheit(k: Double) = celsiusToFahrenheit(kelvinToCelsius(k))
    }

    // Length conversions
    object Length {
        fun metersToFeet(m: Double) = m * 3.28084
        fun feetToMeters(ft: Double) = ft / 3.28084

        fun metersToInches(m: Double) = m * 39.3701
        fun inchesToMeters(inch: Double) = inch / 39.3701

        fun feetToInches(ft: Double) = ft * 12
        fun inchesToFeet(inch: Double) = inch / 12
    }

    // Mass conversions
    object Mass {
        fun kilogramsToPounds(kg: Double) = kg * 2.20462
        fun poundsToKilograms(lb: Double) = lb / 2.20462

        fun kilogramsToOunces(kg: Double) = kg * 35.274
        fun ouncesToKilograms(oz: Double) = oz / 35.274

        fun poundsToOunces(lb: Double) = lb * 16
        fun ouncesToPounds(oz: Double) = oz / 16
    }
}