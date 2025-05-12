package com.dara.unitconverter.ui

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dara.unitconverter.R
import com.dara.unitconverter.data.Length
import com.dara.unitconverter.data.Mass
import com.dara.unitconverter.data.Temperature

@Composable
fun ConversionScreen(modifier: Modifier) {
    val unitTypes = listOf(
        stringResource(R.string.temperature),
        stringResource(R.string.length),
        stringResource(R.string.mass)
    )

    val (selectedUnitType, setSelectedUnitType) = remember { mutableStateOf("") }

    val unitOptions = when (selectedUnitType) {
        stringResource(R.string.temperature) -> Temperature.entries.map { it.name }
        stringResource(R.string.length) -> Length.entries.map { it.name }
        stringResource(R.string.mass) -> Mass.entries.map { it.name }
        else -> emptyList()
    }

    val (initialUnit, setInitialUnit) = remember { mutableStateOf("") }
    val (targetUnit, setTargetUnit) = remember { mutableStateOf("") }

    Column(modifier.padding(vertical = 48.dp, horizontal = 16.dp)) {
        Text(
            stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.select_unit_type_for_conversion),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(12.dp))
        UnitSpinner(
            units = unitTypes,
            onUnitSelected = { selectedUnitType ->
                setSelectedUnitType(selectedUnitType)
                setInitialUnit("")
                setTargetUnit("")
            })
        Spacer(modifier = Modifier.height(12.dp))
        UnitsRow(
            units = unitOptions,
            onInitialUnitSelected = { setInitialUnit(initialUnit) },
            onTargetUnitSelected = { setTargetUnit(targetUnit) })
        ValueFromTextField(unit = initialUnit) { }
        Spacer(modifier = Modifier.height(16.dp))
        ValueToTextField(amount = "", unit = targetUnit)
        ConvertButton()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitSpinner(
    units: List<String>,
    onUnitSelected: (String) -> Unit,
) {
    var selectedUnit by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

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
            )
        }
        Icon(
            modifier = Modifier.padding(horizontal = 12.dp),
            painter = painterResource(R.drawable.ic_swap),
            contentDescription = null
        )
        Box(modifier = Modifier.weight(1f)) {
            UnitSpinner(
                units,
                onUnitSelected = onTargetUnitSelected,
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
        placeholder = { Text("0") },
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
            .background(
                color = Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(amount)
        Text(unit, color = Gray)
    }
}

@Composable
private fun ConvertButton(
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        onClick = {},
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Blue)
    ) {
        Text(stringResource(R.string.convert), style = MaterialTheme.typography.titleLarge)
    }
}
