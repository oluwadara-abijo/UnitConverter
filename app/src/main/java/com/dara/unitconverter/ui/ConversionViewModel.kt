package com.dara.unitconverter.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dara.unitconverter.data.LengthUnit
import com.dara.unitconverter.data.LengthUnit.FEET
import com.dara.unitconverter.data.LengthUnit.INCHES
import com.dara.unitconverter.data.LengthUnit.METERS
import com.dara.unitconverter.data.MassUnit
import com.dara.unitconverter.data.MassUnit.KILOGRAMS
import com.dara.unitconverter.data.MassUnit.OUNCES
import com.dara.unitconverter.data.MassUnit.POUNDS
import com.dara.unitconverter.data.TemperatureUnit
import com.dara.unitconverter.data.TemperatureUnit.CELSIUS
import com.dara.unitconverter.data.TemperatureUnit.FAHRENHEIT
import com.dara.unitconverter.data.TemperatureUnit.KELVIN
import com.dara.unitconverter.data.UnitType.LENGTH
import com.dara.unitconverter.data.UnitType.MASS
import com.dara.unitconverter.data.UnitType.TEMPERATURE
import com.dara.unitconverter.utils.UnitConverter.Length.feetToInches
import com.dara.unitconverter.utils.UnitConverter.Length.feetToMeters
import com.dara.unitconverter.utils.UnitConverter.Length.inchesToFeet
import com.dara.unitconverter.utils.UnitConverter.Length.inchesToMeters
import com.dara.unitconverter.utils.UnitConverter.Length.metersToFeet
import com.dara.unitconverter.utils.UnitConverter.Length.metersToInches
import com.dara.unitconverter.utils.UnitConverter.Mass.kilogramsToOunces
import com.dara.unitconverter.utils.UnitConverter.Mass.kilogramsToPounds
import com.dara.unitconverter.utils.UnitConverter.Mass.ouncesToKilograms
import com.dara.unitconverter.utils.UnitConverter.Mass.ouncesToPounds
import com.dara.unitconverter.utils.UnitConverter.Mass.poundsToKilograms
import com.dara.unitconverter.utils.UnitConverter.Mass.poundsToOunces
import com.dara.unitconverter.utils.UnitConverter.Temperature.celsiusToFahrenheit
import com.dara.unitconverter.utils.UnitConverter.Temperature.celsiusToKelvin
import com.dara.unitconverter.utils.UnitConverter.Temperature.fahrenheitToCelsius
import com.dara.unitconverter.utils.UnitConverter.Temperature.fahrenheitToKelvin
import com.dara.unitconverter.utils.UnitConverter.Temperature.kelvinToCelsius
import com.dara.unitconverter.utils.UnitConverter.Temperature.kelvinToFahrenheit

class ConversionViewModel : ViewModel() {

    private val _uiState = mutableStateOf(ConversionUiState())
    val uiState: State<ConversionUiState> = _uiState

    fun updateUnitType(type: String) {
        val options = when (type) {
            TEMPERATURE.displayName -> TemperatureUnit.entries.map { it.displayName }
            LENGTH.displayName -> LengthUnit.entries.map { it.displayName }
            MASS.displayName -> MassUnit.entries.map { it.displayName }
            else -> emptyList()
        }
        updateState(
            selectedUnitType = type,
            unitOptions = options,
            initialUnit = "",
            targetUnit = ""
        )
    }

    fun updateInitialUnit(unit: String) {
        updateState(initialUnit = unit)
    }

    fun updateTargetUnit(unit: String) {
        updateState(targetUnit = unit)
    }

    fun updateInputValue(value: String) {
        updateState(initialValue = value)
    }

    fun convertTemperature(
        initialUnit: String, targetUnit: String, value: String
    ) {
        val input =
            value.toDoubleOrNull() ?: throw IllegalArgumentException("Invalid numeric input")

        if (initialUnit == targetUnit) {
            updateState(targetValue = input.toString())
            return
        }

        val result = when (initialUnit to targetUnit) {
            CELSIUS.displayName to FAHRENHEIT.displayName -> celsiusToFahrenheit(input)
            CELSIUS.displayName to KELVIN.displayName -> celsiusToKelvin(input)
            FAHRENHEIT.displayName to CELSIUS.displayName -> fahrenheitToCelsius(input)
            FAHRENHEIT.displayName to KELVIN.displayName -> fahrenheitToKelvin(input)
            KELVIN.displayName to CELSIUS.displayName -> kelvinToCelsius(input)
            KELVIN.displayName to FAHRENHEIT.displayName -> kelvinToFahrenheit(input)
            else -> throw IllegalArgumentException("Unsupported temperature conversion: $initialUnit to $targetUnit")
        }
        updateState(targetValue = result.toString())

    }

    fun convertLength(
        initialUnit: String, targetUnit: String, value: String
    ) {
        val input =
            value.toDoubleOrNull() ?: throw IllegalArgumentException("Invalid numeric input")

        if (initialUnit == targetUnit) {
            updateState(targetValue = input.toString())
        } else {
            val result = when (initialUnit to targetUnit) {
                METERS.displayName to FEET.displayName -> metersToFeet(input)
                METERS.displayName to INCHES.displayName -> metersToInches(input)
                FEET.displayName to METERS.displayName -> feetToMeters(input)
                FEET.displayName to INCHES.displayName -> feetToInches(input)
                INCHES.displayName to METERS.displayName -> inchesToMeters(input)
                INCHES.displayName to FEET.displayName -> inchesToFeet(input)
                else -> throw IllegalArgumentException("Unsupported temperature conversion: $initialUnit to $targetUnit")
            }
            updateState(targetValue = result.toString())
        }

    }

    fun convertMass(
        initialUnit: String, targetUnit: String, value: String
    ) {
        val input =
            value.toDoubleOrNull() ?: throw IllegalArgumentException("Invalid numeric input")

        if (initialUnit == targetUnit) {
            updateState(targetValue = input.toString())
        } else {
            val result = when (initialUnit to targetUnit) {
                KILOGRAMS.displayName to POUNDS.displayName -> kilogramsToPounds(input)
                KILOGRAMS.displayName to OUNCES.displayName -> kilogramsToOunces(input)
                POUNDS.displayName to KILOGRAMS.displayName -> poundsToKilograms(input)
                POUNDS.displayName to OUNCES.displayName -> poundsToOunces(input)
                OUNCES.displayName to KILOGRAMS.displayName -> ouncesToKilograms(input)
                OUNCES.displayName to POUNDS.displayName -> ouncesToPounds(input)
                else -> throw IllegalArgumentException("Unsupported temperature conversion: $initialUnit to $targetUnit")
            }
            updateState(targetValue = result.toString())
        }

    }

    // Updates the current state of the UI given the necessary values
    private fun updateState(
        selectedUnitType: String? = null,
        unitOptions: List<String>? = null,
        initialUnit: String? = null,
        targetUnit: String? = null,
        initialValue: String? = null,
        targetValue: String? = null,
    ) {
        _uiState.value = _uiState.value.copy(
            selectedUnitType = selectedUnitType ?: _uiState.value.selectedUnitType,
            unitOptions = unitOptions ?: _uiState.value.unitOptions,
            initialUnit = initialUnit ?: _uiState.value.initialUnit,
            targetUnit = targetUnit ?: _uiState.value.targetUnit,
            initialValue = initialValue ?: _uiState.value.initialValue,
            targetValue = targetValue ?: _uiState.value.targetValue,
        )
    }
}
