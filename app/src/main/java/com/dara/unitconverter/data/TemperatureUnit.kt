package com.dara.unitconverter.data

/**
 * Represents different temperature units.
 *
 * This enum class provides constants for the three temperature unit options: Celsius,
 * Fahrenheit, and Kelvin.
 * [CELSIUS] is the base unit and conversions from other units are relative to it.
 */
enum class TemperatureUnit(val displayName: String) {
    CELSIUS("Celsius"),
    FAHRENHEIT("Fahrenheit"),
    KELVIN("Kelvin");

    companion object {
        /**
         * Returns the TemperatureUnit enum constant with the specified display name,
         * ignoring case.
         *
         * @param name The display name of the enum constant.
         * @return The enum constant with the specified display name,
         * or null if no such constant exists.
         */
        fun fromDisplayName(name: String): TemperatureUnit? {
            return TemperatureUnit.entries.find {
                it.displayName.equals(name, ignoreCase = true)
            }
        }
    }

    /**
     * Converts a temperature value to Celsius based on the current temperature unit.
     *
     * @param value The temperature value to convert.
     * @return The temperature value in Celsius.
     */
    fun toCelsius(value: Double): Double = when (this) {
        CELSIUS -> value
        FAHRENHEIT -> (value - 32) * 5.0 / 9.0
        KELVIN -> value - 273.15
    }

    /**
     * Converts a temperature value from Celsius to the unit represented by the
     * current enum instance.
     *
     * @param celsiusValue The temperature value in Celsius.
     * @return The converted temperature value.
     */
    fun fromCelsius(celsiusValue: Double?): Double =
        if (celsiusValue == null) {
            0.0
        } else {
            when (this) {
                CELSIUS -> celsiusValue
                FAHRENHEIT -> celsiusValue * 9.0 / 5.0 + 32
                KELVIN -> celsiusValue + 273.15
            }
        }

}
