package com.dara.unitconverter.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dara.unitconverter.data.Length
import com.dara.unitconverter.data.Mass
import com.dara.unitconverter.data.Temperature
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
            "Temperature" -> Temperature.entries.map { it.name }
            "Length" -> Length.entries.map { it.name }
            "Mass" -> Mass.entries.map { it.name }
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
        } else {
            val result = when (initialUnit to targetUnit) {
                "Celsius" to "Fahrenheit" -> celsiusToFahrenheit(input)
                "Celsius" to "Kelvin" -> celsiusToKelvin(input)
                "Fahrenheit" to "Celsius" -> fahrenheitToCelsius(input)
                "Fahrenheit" to "Kelvin" -> fahrenheitToKelvin(input)
                "Kelvin" to "Celsius" -> kelvinToCelsius(input)
                "Kelvin" to "Fahrenheit" -> kelvinToFahrenheit(input)
                else -> throw IllegalArgumentException("Unsupported temperature conversion: $initialUnit to $targetUnit")
            }
            updateState(targetValue = result.toString())
        }


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
                "Meters" to "Feet" -> metersToFeet(input)
                "Meters" to "Inches" -> metersToInches(input)
                "Feet" to "Meters" -> feetToMeters(input)
                "Feet" to "Inches" -> feetToInches(input)
                "Inches" to "Meters" -> inchesToMeters(input)
                "Inches" to "Feet" -> inchesToFeet(input)
                else -> throw IllegalArgumentException("Unsupported length conversion: $initialUnit to $targetUnit")
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
                "Kilograms" to "Pounds" -> kilogramsToPounds(input)
                "Kilograms" to "Ounces" -> kilogramsToOunces(input)
                "Pounds" to "Kilograms" -> poundsToKilograms(input)
                "Pounds" to "Ounces" -> poundsToOunces(input)
                "Ounces" to "Kilograms" -> ouncesToKilograms(input)
                "Ounces" to "Pounds" -> ouncesToPounds(input)
                else -> throw IllegalArgumentException("Unsupported mass conversion: $initialUnit to $targetUnit")
            }
            updateState(targetValue = result.toString())
        }

    }

    /**
     * Swaps the initial and target units for the current conversion.
     *
     * This function updates the UI state by exchanging the values of [initialUnit] and [targetUnit].
     * After the swap, it triggers a new conversion based on the updated units and the current input value.
     * The conversion performed depends on the currently selected unit type ([selectedUnitType]).
     *
     * - If the selected unit type is "Temperature", [convertTemperature] is called.
     * - If the selected unit type is "Length", [convertLength] is called.
     * - If the selected unit type is "Mass", [convertMass] is called.
     */
    fun swapConversion() {
        val newInitialUnit = _uiState.value.targetUnit
        val newTargetUnit = _uiState.value.initialUnit
        updateState(
            initialUnit = newInitialUnit,
            targetUnit = newTargetUnit
        )
        when (_uiState.value.selectedUnitType) {
            "Temperature" -> convertTemperature(
                uiState.value.initialUnit,
                uiState.value.targetUnit,
                uiState.value.initialValue
            )

            "Length" -> convertLength(
                uiState.value.initialUnit,
                uiState.value.targetUnit,
                uiState.value.initialValue
            )

            "Mass" -> convertMass(
                uiState.value.initialUnit,
                uiState.value.targetUnit,
                uiState.value.initialValue
            )
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
