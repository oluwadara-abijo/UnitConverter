package com.dara.unitconverter.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dara.unitconverter.R
import com.dara.unitconverter.data.UnitType.LENGTH
import com.dara.unitconverter.data.UnitType.MASS
import com.dara.unitconverter.data.UnitType.TEMPERATURE
import com.dara.unitconverter.utils.UnitConverter.isValidAmount

@Composable
fun ConversionScreen(
    modifier: Modifier,
    viewModel: ConversionViewModel = viewModel()
) {
    val uiState by viewModel.uiState
    val context = LocalContext.current

    val unitTypes = listOf(
        TEMPERATURE.displayName,
        MASS.displayName,
        LENGTH.displayName
    )

    Column(modifier.padding(vertical = 48.dp, horizontal = 16.dp)) {
        Text(
            stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(48.dp))
        UnitSpinner(
            units = unitTypes,
            onUnitSelected = { selectedUnitType ->
                viewModel.updateUnitType(selectedUnitType)
            },
            label = stringResource(R.string.select_unit_type_for_conversion)
        )
        UnitsRow(
            units = uiState.unitOptions,
            onInitialUnitSelected = { initialUnit -> viewModel.updateInitialUnit(initialUnit) },
            onTargetUnitSelected = { targetUnit -> viewModel.updateTargetUnit(targetUnit) })
        ValueFromTextField(unit = uiState.initialUnit) { initialValue ->
            viewModel.updateInputValue(initialValue)
        }
        Spacer(modifier = Modifier.height(16.dp))
        ValueToTextField(amount = uiState.targetValue, unit = uiState.targetUnit)
        ConvertButton(uiState, viewModel, context)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitSpinner(
    units: List<String>,
    onUnitSelected: (String) -> Unit,
    label: String
) {
    var selectedUnit by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = label,
            modifier = Modifier.padding(bottom = 4.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        ExposedDropdownMenuBox(
            expanded = isDropdownExpanded,
            onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                    .fillMaxWidth(),
                value = selectedUnit,
                onValueChange = { },
                readOnly = true,
                singleLine = true,
                trailingIcon = {
                    if (isDropdownExpanded) Icon(
                        painterResource(R.drawable.ic_expand_less),
                        contentDescription = null
                    ) else {
                        Icon(
                            painterResource(R.drawable.ic_expand_more),
                            contentDescription = null
                        )
                    }
                }
            )
            ExposedDropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }) {
                units.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit) },
                        onClick = {
                            selectedUnit = unit
                            isDropdownExpanded = false
                            onUnitSelected(unit)
                        })
                }
            }
        }
    }
}

@Composable
fun UnitsRow(
    units: List<String>,
    onInitialUnitSelected: (String) -> Unit,
    onTargetUnitSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            UnitSpinner(
                units = units,
                onUnitSelected = onInitialUnitSelected,
                label = "From"
            )
        }
        Icon(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 32.dp),
            painter = painterResource(R.drawable.ic_swap),
            contentDescription = null
        )
        Box(modifier = Modifier.weight(1f)) {
            UnitSpinner(
                units,
                onUnitSelected = onTargetUnitSelected,
                label = "To"
            )
        }
    }
}

@Composable
private fun ValueFromTextField(
    unit: String,
    onValueChanged: (String) -> Unit,
) {
    var amount by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = amount,
        onValueChange = {
            amount = it
            onValueChanged(it)
        },
        trailingIcon = { Text(unit, color = Gray) },
        singleLine = true,
        placeholder = { Text("Enter value to convert") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Gray.copy(alpha = 0.2f),
            unfocusedContainerColor = Gray.copy(alpha = 0.2f),
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
fun ValueToTextField(amount: String, unit: String) {
    Row(
        modifier = Modifier
            .background(color = Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            amount,
            style = MaterialTheme.typography.headlineSmall,
            color = Blue
        )
        Text(unit, color = Gray)
    }
}

@Composable
private fun ConvertButton(
    uiState: ConversionUiState,
    viewModel: ConversionViewModel,
    context: Context
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        enabled = (uiState.initialUnit.isNotBlank() && uiState.targetUnit.isNotBlank()),
        onClick = {
            val (isValid, errorMessage) = isValidAmount(uiState.initialValue)
            if (isValid) {
                when (uiState.selectedUnitType) {
                    TEMPERATURE.displayName -> viewModel.convertTemperature(
                        uiState.initialUnit,
                        uiState.targetUnit,
                        uiState.initialValue
                    )

                    LENGTH.displayName -> viewModel.convertLength(
                        uiState.initialUnit,
                        uiState.targetUnit,
                        uiState.initialValue
                    )

                    MASS.displayName -> viewModel.convertMass(
                        uiState.initialUnit,
                        uiState.targetUnit,
                        uiState.initialValue
                    )
                }
            } else {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        },
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Blue)
    ) {
        Text(stringResource(R.string.convert), style = MaterialTheme.typography.titleLarge)
    }
}
