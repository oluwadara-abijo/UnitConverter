package com.dara.unitconverter.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dara.unitconverter.data.LengthUnit
import com.dara.unitconverter.data.MassUnit
import com.dara.unitconverter.data.TemperatureUnit
import com.dara.unitconverter.data.UnitType.LENGTH
import com.dara.unitconverter.data.UnitType.MASS
import com.dara.unitconverter.data.UnitType.TEMPERATURE

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
        initialUnitString: String,
        targetUnitString: String,
        temperatureValue: String
    ) {
        val input = temperatureValue.toDouble()

        if (initialUnitString == targetUnitString) {
            updateState(targetValue = input.toString())
            return
        }

        val initialUnit = TemperatureUnit.fromDisplayName(initialUnitString)
        val targetUnit = TemperatureUnit.fromDisplayName(targetUnitString)


        val valueInCelsius = initialUnit?.toCelsius(input)
        val result = targetUnit?.fromCelsius(valueInCelsius)

        updateState(targetValue = result.toString())

    }

    fun convertLength(
        initialUnitString: String,
        targetUnitString: String,
        lengthValue: String
    ) {
        val input = lengthValue.toDouble()

        if (initialUnitString == targetUnitString) {
            updateState(targetValue = input.toString())
            return
        }

        val initialUnit = LengthUnit.fromDisplayName(initialUnitString)
        val targetUnit = LengthUnit.fromDisplayName(targetUnitString)


        val valueInCelsius = initialUnit?.toMeters(input)
        val result = targetUnit?.fromMeters(valueInCelsius)

        updateState(targetValue = result.toString())

    }

    fun convertMass(
        initialUnitString: String,
        targetUnitString: String,
        massValue: String
    ) {
        val input = massValue.toDouble()

        if (initialUnitString == targetUnitString) {
            updateState(targetValue = input.toString())
            return
        }

        val initialUnit = MassUnit.fromDisplayName(initialUnitString)
        val targetUnit = MassUnit.fromDisplayName(targetUnitString)

        val valueInCelsius = initialUnit?.toKilograms(input)
        val result = targetUnit?.fromKilograms(valueInCelsius)

        updateState(targetValue = result.toString())

    }

    /**
     * Swaps the initial and target units for the current conversion.
     *
     * This function updates the UI state by exchanging the values of initialUnit and targetUnit.
     * After the swap, it triggers a new conversion based on the updated units and the current input value.
     * The conversion performed depends on the currently selected unit type selectedUnitType.
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
