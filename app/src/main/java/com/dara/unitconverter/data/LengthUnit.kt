package com.dara.unitconverter.data

import com.dara.unitconverter.data.LengthUnit.METERS


/**
 * Represents different length units.
 *
 * This enum class provides constants for the three mass unit
 * options: Meters, Feet, and Inches.
 * [METERS] is the base unit and conversions from other units are relative to it.
 *
 */

enum class LengthUnit(val displayName: String) {
    METERS("Meters"),
    FEET("Feet"),
    INCHES("Inches");

    companion object {
        /**
         * Returns the LengthUnit enum constant with the specified display name, ignoring case.
         *
         * @param name The display name of the enum constant.
         * @return The enum constant with the specified display name,or null if no such constant exists.
         */
        fun fromDisplayName(name: String): LengthUnit? {
            return LengthUnit.entries.find {
                it.displayName.equals(name, ignoreCase = true)
            }
        }
    }

    /**
     * Converts a length value to Meters based on the current length unit.
     *
     * @param value The length value to convert.
     * @return The length value in Meters.
     */
    fun toMeters(value: Double): Double = when (this) {
        METERS -> value
        FEET -> value * 0.3048 // 1 foot = 0.3048 meters
        INCHES -> value * 0.0254 // 1 inch = 0.0254 meters
    }

    /**
     * Converts a length value from Meters to the unit represented by the
     * current enum instance.
     *
     * @param metersValue The length value in Meters.
     * @return The converted length value.
     */
    fun fromMeters(metersValue: Double?): Double =
        if (metersValue == null) {
            0.0
        } else {
            when (this) {
                METERS -> metersValue
                FEET -> metersValue / 0.3048 // Convert meters to feet
                INCHES -> metersValue / 0.0254 // Convert meters to inches
            }
        }

}
