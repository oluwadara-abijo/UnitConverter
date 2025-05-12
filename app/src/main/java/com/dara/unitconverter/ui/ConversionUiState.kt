package com.dara.unitconverter.ui

data class ConversionUiState(
    val selectedUnitType: String = "",
    val unitOptions: List<String> = emptyList(),
    val initialUnit: String = "",
    val targetUnit: String = "",
    val initialValue: String = "0",
    val targetValue: String = "",
)
