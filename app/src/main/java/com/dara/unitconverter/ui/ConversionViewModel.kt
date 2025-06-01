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
