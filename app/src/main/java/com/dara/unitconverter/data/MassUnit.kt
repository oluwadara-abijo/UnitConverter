package com.dara.unitconverter.data

import com.dara.unitconverter.data.MassUnit.KILOGRAMS


/**
 * Represents different mass units.
 *
 * This enum class provides constants for the three mass unit options: Kilograms, Pounds, and Ounces.
 * [KILOGRAMS] is the base unit and conversions from other units are relative to it.
 *
 */
enum class MassUnit(val displayName: String) {
    KILOGRAMS("Kilograms"),
    POUNDS("Pounds"),
    OUNCES("Ounces");

    companion object {
        /**
         * Returns the MassUnit enum constant with the specified display name, ignoring case.
         *
         * @param name The display name of the enum constant.
         * @return The enum constant with the specified display name,or null if no such constant exists.
         */
        fun fromDisplayName(name: String): MassUnit? {
            return entries.find {
                it.displayName.equals(name, ignoreCase = true)
            }
        }
    }

    /**
     * Converts a mass value from the current unit to Kilograms (the base unit).
     *
     * @param value The mass value in the current unit.
     * @return The mass value in Kilograms.
     */
    fun toKilograms(value: Double): Double = when (this) {
        KILOGRAMS -> value
        POUNDS -> value * 0.453592 // 1 pound = 0.453592 kilograms
        OUNCES -> value * 0.0283495 // 1 ounce = 0.0283495 kilograms
    }

    /**
     * Converts a mass value from Kilograms (the base unit) to the current unit.
     *
     * @param kilogramsValue The mass value in Kilograms.
     * @return The converted mass value in the current unit.
     */
    fun fromKilograms(kilogramsValue: Double?): Double =
        if (kilogramsValue == null) {
            0.0
        } else {
            when (this) {
                KILOGRAMS -> kilogramsValue
                POUNDS -> kilogramsValue / 0.453592
                OUNCES -> kilogramsValue / 0.0283495
            }
        }
}
