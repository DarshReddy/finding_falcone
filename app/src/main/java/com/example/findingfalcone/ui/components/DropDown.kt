package com.example.findingfalcone.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.toSize
import com.example.findingfalcone.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> DropDown(
    label: String,
    items: List<T>,
    getItemText: (T) -> String,
    getItemCount: ((T) -> Int)? = null,
    selectItem: (String) -> Unit,
    shouldReset: Boolean
) {
    var exp by remember { mutableStateOf(false) }

    var selectedOption by remember { mutableStateOf("") }
    if (shouldReset) selectedOption = ""

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (exp)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = { selectedOption = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to
                    // the DropDown the same width
                    textFieldSize = coordinates.size.toSize()
                },
            label = { Text(stringResource(id = R.string.select_label, label)) },
            trailingIcon = {
                Icon(icon, stringResource(id = R.string.drop_down_arrow),
                    Modifier.clickable { exp = !exp }
                )
            }
        )

        if (items.isNotEmpty()) {
            DropdownMenu(
                modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() }),
                expanded = exp,
                onDismissRequest = { exp = false },
            ) {
                items.forEach { option ->
                    DropdownMenuItem(
                        modifier = Modifier.testTag(
                            stringResource(
                                id = R.string.drop_down_menu_item_tag,
                                getItemText(option)
                            )
                        ),
                        text = {
                            Text(
                                text = getItemCount?.let {
                                    stringResource(
                                        id = R.string.vehicle_info,
                                        getItemText(option),
                                        it.invoke(option)
                                    )
                                } ?: getItemText(option)
                            )
                        },
                        onClick = {
                            selectedOption = getItemText(option)
                            exp = false
                            selectItem(getItemText(option))
                        }
                    )
                }
            }
        }
    }
}